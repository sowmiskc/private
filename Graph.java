import java.util.*;
import java.io.*;

public class Graph{
	private int NumVertices; //Number of vertices in the graph
	private Vertex[] Vertices; //Adjacency list that represents the edges between two vertices in the graph
	private ArrayList<Edge> Edges; //To keep track of later for minimum spanning trees (Kruskal's algorithm)
	private ArrayList<Edge> MinSpanningTreeWithLowestLatency; //Dynamic programming solution minimum spanning tree; allows for O(1) lookup when accessed later
	private double LowestAverageLatency; //Global variable used for the minimum spanning tree; helps with a dynamic programming solution for increased program runtime
	private boolean CopperOnlyConnected = true; //Used for one of the menu options in NetworkAnalysis; keeps track of whether or not there are only copper wires in the graph

	public Graph(String filename){
		constructGraph(filename);
	}

	//Find the path in the graph that connects two user-specificed vertices with the lowest
	//	total latency.


	//Find out whether graph is connected with only copper, connected considering only copper, or neither
	public void determineCopperConnectivity(){
		if(CopperOnlyConnected){ //The graph consists of only copper wires
			System.out.println("-- This graph consists of only copper wires, so it is copper-connected.");
		} else{
			boolean connectedWithCopper = true; //Assume the graph is connected with only copper

			for(int i = 0; i < NumVertices; i++){ //Iterate through every vertex and check to make sure it has at least one copper connection
				LinkedList<Edge> vertexEdges = Vertices[i].getConnections();

				boolean hasCopperConnection = false;
				for(Edge edge : vertexEdges){
					if(edge.getMaterial().equals("copper")){ //There exists a copper wire from this vertex
						hasCopperConnection = true;
						break;
					}
				}

				if(!hasCopperConnection){ //If this vertex does not have a single copper connection, then the graph cannot be copper connected
					connectedWithCopper = false;
					break;
				}
			}

			if(connectedWithCopper)	System.out.println("-- This graph can be connected with only copper wires.  But, this graph also has fiber optic wires.");
			else System.out.println("-- This graph is not copper-only and cannot be connected with only copper wires.");
		}
	}

	//Find the path between two vertices that allows the maximum amount of data transfer at one time
	public void findMaxDataPath(){
		Scanner sc = new Scanner(System.in);
		Vertex vertexStart = null;
		Vertex vertexEnd = null;

		//Ask the user for the starting vertex
		do{
			System.out.print("Enter ID of first vertex: ");
			int id = Integer.parseInt(sc.nextLine());
			if(id >= NumVertices || id < 0) System.out.println("\n=== Invalid vertex! ===\n");
			else vertexStart = Vertices[id];
		} while(vertexStart == null);

		//Ask the user for the destination vertex
		do{
			System.out.print("Enter ID of second vertex: ");
			int id = Integer.parseInt(sc.nextLine());
			if(id >= NumVertices || id < 0 || id == vertexStart.getID()) System.out.println("\n=== Invalid vertex! ===\n");
			else vertexEnd = Vertices[id];
		} while(vertexEnd == null);

		//Calculate all of the paths between the vertices and print out the statistics of the path with the maximum bandwidth
		int maxData = maxDataBetweenTwoVertices(vertexStart, vertexEnd, "" + vertexStart.getID(), -1);
		System.out.println("\nMax amount of data between vertices " + vertexStart.getID() + " and " + vertexEnd.getID() + ": " + maxData + " Mbps");
	}

	//When any two vertices fail, determine whether or not the graph is still connected
	public void findConnectivityOnVertexFailures(){
		//Permute all possible pairs of vertices failing and calculate if the graph is connected or not
		for(int i = 0; i < NumVertices-1; i++){
			for(int j = i+1; j < NumVertices; j++){
				//Traverse the graph ignoring Vertices[i] and Vertices[j]
				//If the path's length ever reaches NumVertices-2, then it is connected
				Vertex startVertex = null;
				Vertex failureOne = Vertices[i];
				Vertex failureTwo = Vertices[j];
				boolean[] visited = new boolean[NumVertices];

				//Mark that the failed vertices have already been visited so the traversal doesn't visit them
				visited[failureOne.getID()] = true;
				visited[failureTwo.getID()] = true;

				//Set the starting vertex for to explore from; it can be any vertex that isn't in the pair of failing vertices
				if(i != 0){ //If we're omitting vertex 0, then make sure we don't start there
					startVertex = Vertices[0];
				} else{ //Vertex 0 failed, so determine a new vertex that didn't fail
					if(j != NumVertices-1){
						startVertex = Vertices[j+1];
					} else if(j-i != 1){
						startVertex = Vertices[j-1];
					} else{
						System.out.println("-- This graph IS NOT connected when any two vertices fail."); //There are only 2 vertices in the graph so if both fail, it is not connected
						return;
					}
				}

				//Pass in the visited array and mark the vertices that were traversed across
				findConnectivityWithoutTwoVertices(startVertex, failureOne, failureTwo, visited);

				//Check to make sure all vertices were visited; in other words, the graph is still connected despite the failures
				boolean graphIsConnected = true;
				for(int k = 0; k < visited.length; k++){
					if(visited[k] == false){ //A node was not visited, so the graph is not connected
						graphIsConnected = false;
						break;
					}
				}

				if(!graphIsConnected){ //If we find out that any two pairs of vertices failing causes the graph to not be connected, return
					System.out.println("-- This graph IS NOT connected when any two vertices fail.");
					return;
				}
			}
		}

		System.out.println("-- This graph IS connected when any two vertices fail.");
		return; //All possible combinations of two vertices failing produced connected graphs
	}

	//Calculate the minimum spanning tree with the lowest average latency among the edges.
	//Another definition of this is the tree that allows for the fastest data transfer across the entire graph.


	private void constructGraph(String filename){
		if(filename == null) return;

		//Construct the file and make sure it exists before scanning its contents
		File f = new File(filename);
		Scanner sc;
		try{
			sc = new Scanner(f);
		} catch(FileNotFoundException e){
			System.out.println("=== File not found! ===");
			return;
		}

		if(sc.hasNextLine()) NumVertices = Integer.parseInt(sc.nextLine()); //The number of vertices in the graph is the first line
		else return; //We can't construct a graph if we don't know how many vertices there are

		Vertices = new Vertex[NumVertices]; //Initialize the adjacency list that contains <# of vertices> Vertices
		for(int i = 0; i < Vertices.length; i++){
			Vertices[i] = new Vertex(i); //Initialize vertex i with its proper ID
		}

		Edges = new ArrayList<Edge>();
		while(sc.hasNextLine()){ //Run through the file and generate all the edges of the graph
			String line = sc.nextLine();
			String[] lineContents = line.split(" ");
			if(lineContents.length != 5)  continue; //Each line must contain two vertices, a material, bandwidth, and length (5 items)

			//Separate the contents of the line into their respective variables to add them to edges in the graph
			Vertex vertexA = Vertices[Integer.parseInt(lineContents[0])];
			Vertex vertexB = Vertices[Integer.parseInt(lineContents[1])];
			String material = lineContents[2];
			int bandwidth = Integer.parseInt(lineContents[3]); //Maximum amount of data that can travel along this edge
			int length = Integer.parseInt(lineContents[4]); //Length of the edge

			//Create two new edges between the two vertices and make sure they map back and forth to eachother (they're full duplex)
			Edge edgeFromAtoB = new Edge(material, bandwidth, length, vertexB, vertexA);
			Edge edgeFromBtoA = new Edge(material, bandwidth, length, vertexA, vertexB);
			vertexA.getConnections().addFirst(edgeFromAtoB);
			vertexB.getConnections().addFirst(edgeFromBtoA);
			Edges.add(edgeFromAtoB); //Add one of the edges to the edges list because only one is needed for a minimum spanning tree
			if(material.equals("optical")) CopperOnlyConnected = false; //If at any point while constructing this graph there is an optical fiber, then it is not copper-only connected
		}
	}

	private void findConnectivityWithoutTwoVertices(Vertex curr, Vertex a, Vertex b, boolean[] visited){
		if(curr == null || a == null || b == null || visited == null) return; //Any invalid input will return null promptly

		if(visited[curr.getID()] == true){ //This node has already been visited
			return;
		}

		visited[curr.getID()] = true; //Mark that the current node has been visited

		LinkedList<Edge> currEdges = curr.getConnections();

		for(Edge edge : currEdges){ //Perform a depth-first search to attempt to traverse all nodes except for the failed ones in the graph
			Vertex edgeDestination = edge.getDestination(); //Get the destination Vertex of this current edge
			if(visited[edgeDestination.getID()] == true) continue; //We already traversed to this node, so don't cycle back to it

			findConnectivityWithoutTwoVertices(edgeDestination, a, b, visited); //Recursively traverse the graph
		}

		return;
	}

	//Merges the component containing site p with the
	//	the component containing site q.
    private void union(int p, int q, int[] parent, byte[] rank) {
        int rootP = find(p, parent);
        int rootQ = find(q, parent);
        if (rootP == rootQ) return;

        //Make root of smaller rank point to root of larger rank
        if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }

	//Returns true if the the two sites are in the same component.
	//Indicates later on if the two vertices connecting will cause a cycle in the graph.
	private boolean connected(int p, int q, int[] parent) {
        return find(p, parent) == find(q, parent);
    }

	//Returns the component identifier for the component containing site p.
	private int find(int p, int[] parent) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];    // path compression by halving
            p = parent[p];
        }
        return p;
    }
}