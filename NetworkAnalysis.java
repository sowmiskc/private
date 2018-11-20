import java.util.*;
public class NetworkAnalysis {
  public static void main (String [] args){
	  String filename = ""; 
	  if (args.length > 0){
		  filename = args[0]; 
	  }
	  Graph graph = new Graph (filename); 
	 //graph.findLowestLatencyPath();
	 boolean ask = true; 
	 
	 Scanner scan = new Scanner (System.in); 
	 int userinput = 0; 
	 while (ask){
		 System.out.println("1. Find the lowest latency path");
		 System.out.println("2. Determine copper connectivity");
		 System.out.println("3. Find max data that can be transferred from each vertex");
		 System.out.println("4. Determine if graph is dissconnected when two vertices failed"); 
		 System.out.println("5. Find lowest latency spanning tree");
		 System.out.println("6. Quit program");
		 System.out.print("Enter Selection: "); 
		 userinput = scan.nextInt(); 
		if (userinput == 1){
			graph.findLowestLatencyPath();
		}
		if (userinput == 2){
			graph.determineCopperConnectivity();
		}
		if (userinput == 3){
			graph.findMaxDataPath();
		}
		if (userinput == 4){
			graph.findConnectivityOnVertexFailures();
		}
		if (userinput == 5){
			graph.findLowestLatencySpanningTree();
		}
		if (userinput == 6){
			System.exit(0);
		}
		
	 }
	  
  }
}
