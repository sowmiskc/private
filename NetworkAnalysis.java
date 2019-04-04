//driver program which requires the use other java files to run
//reliant on files called: AllPaths.java, DijkstraAlgorithm.java, Edge.java, Graph.java, KruskalMST.java, MinPQ.java
//Queue.java, UF.java, Vertex.java

import java.util.*;

public class NetworkAnalysis {
  
  public static void main (String [] args){
	  
	  String file = ""; 
	  
	  if (args.length > 0){
		  file = args[0]; 
	  }
	  
	  Graph graph = new Graph (file); 
	 
	 boolean ask = true; 
	 
	 Scanner scan = new Scanner (System.in); 
	 int option = 0; 
	 
	 while (ask){
		 System.out.println("1: Find the lowest latency path between two points, and give the bandwidth available along that path");
		 System.out.println("2: Determine whether or not the graph is copper-only connected, or whether it is connected considering only copper links.");
		 System.out.println("3: Find the minimum average spanning tree.");
		 System.out.println("4: Determine whether or not the graph would remain connected if any two vertices in the graph were to fail."); 
		 System.out.println("5: Quit the program");
		 System.out.print("Select an option 1-5 from above: "); 
		 option = scan.nextInt(); 
		
		if (option == 1){
			graph.findLowestLatencyPath();
		}
		if (option == 2){
			graph.determineCopperConnectivity();
		}
		if (option == 3){
			graph.findLowestLatencySpanningTree();
		}
		if (option == 4){
			graph.findConnectivityOnVertexFailures();
		}
		if (option == 5){
			System.exit(0);
		}
		
	 }
	  
  }
}
