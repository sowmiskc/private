import java.util.*;
import java.io.*;

public class Graph{
	private int NumVertices; 
	private Vertex[] Vertices; 
	private ArrayList<Edge> Edges; 
	private boolean CopperOnlyConnected = true; 

	public Graph(String file){
		constructGraph(file);
	}

	//Copper connection determination
	public void determineCopperConnectivity(){
		if(CopperOnlyConnected){ 
			System.out.println("-- This graph consists of only copper wires, so it is copper-connected.");
		} else{
			boolean connectedWithCopper = true; 
			for(int i = 0; i < NumVertices; i++){  
				LinkedList<Edge> vertexEdges = Vertices[i].getConnections();

				boolean hasCopperConnection = false;
				for(Edge edge : vertexEdges){
					if(edge.getMaterial().equals("copper")){ 
						hasCopperConnection = true;
						break;
					}
				}

				if(!hasCopperConnection){
					connectedWithCopper = false;
					break;
				}
			}

			if(connectedWithCopper)	System.out.println("-- This graph can be connected with only copper wires.  But, this graph also has fiber optic wires.");
			else System.out.println("-- This graph is not copper-only and cannot be connected with only copper wires.");
		}
	}

	
	public void findMaxDataPath(){
		Scanner sc = new Scanner(System.in);
		Vertex vertexStart = null;
		Vertex vertexEnd = null;

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

		int maxData = maxDataBetweenTwoVertices(vertexStart, vertexEnd, "" + vertexStart.getID(), -1);
		System.out.println("\nMax amount of data between vertices " + vertexStart.getID() + " and " + vertexEnd.getID() + ": " + maxData + " Mbps");
	}
	public void findConnectivityOnVertexFailures(){
		for(int i = 0; i < NumVertices-1; i++){
			for(int j = i+1; j < NumVertices; j++){
				Vertex startVertex = null;
				Vertex failureOne = Vertices[i];
				Vertex failureTwo = Vertices[j];
				boolean[] visited = new boolean[NumVertices];
				visited[failureOne.getID()] = true;
				visited[failureTwo.getID()] = true;

				if(i != 0){ 
					startVertex = Vertices[0];
				} else{ 
					if(j != NumVertices-1){
						startVertex = Vertices[j+1];
					} else if(j-i != 1){
						startVertex = Vertices[j-1];
					} else{
						System.out.println("-- This graph IS NOT connected when any two vertices fail."); 
						return;
					}
				}

				findConnectivityWithoutTwoVertices(startVertex, failureOne, failureTwo, visited);

				boolean graphIsConnected = true;
				for(int k = 0; k < visited.length; k++){
					if(visited[k] == false){ 
						graphIsConnected = false;
						break;
					}
				}

				if(!graphIsConnected){ 
					System.out.println("-- This graph IS NOT connected when any two vertices fail.");
					return;
				}
			}
		}

		System.out.println("-- This graph IS connected when any two vertices fail.");
		return; 
	}

	private void findConnectivityWithoutTwoVertices(Vertex curr, Vertex a, Vertex b, boolean[] visited){
		if(curr == null || a == null || b == null || visited == null) return; 

		if(visited[curr.getID()] == true){ 
			return;
		}

		visited[curr.getID()] = true; 
		LinkedList<Edge> currEdges = curr.getConnections();

		for(Edge edge : currEdges){ 
			Vertex edgeDestination = edge.getDestination(); 
			if(visited[edgeDestination.getID()] == true) continue;  

			findConnectivityWithoutTwoVertices(edgeDestination, a, b, visited); 
		}

		return;
	}

    private void union(int p, int q, int[] parent, byte[] rank) {
        int rootP = find(p, parent);
        int rootQ = find(q, parent);
        if (rootP == rootQ) return;

        if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }

	private boolean connected(int p, int q, int[] parent) {
        return find(p, parent) == find(q, parent);
    }


	private int find(int p, int[] parent) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];    
            p = parent[p];
        }
        return p;
    }
}
