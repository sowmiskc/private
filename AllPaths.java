import java.util.*;

public class AllPaths{

    private Stack<Vertex> path  = new Stack<Vertex>();   // the current path
    private ArrayList<Vertex> onPath  = new ArrayList<Vertex>();     // the set of vertices on the path

    public AllPaths(Graph G, Vertex s, Vertex t, String[] bestPath ) {
        enumerate(G, s, t, new int [1], bestPath);
    }

    // use DFS
    private void enumerate(Graph G, Vertex v, Vertex t, int [] maxBandwidth, String [] bestPath) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);

        // found path from s to t - currently prints in reverse order because of stack
        if (v.equals(t)) 
        {
        	String text = ""; 
        	int bandWidth = Integer.MAX_VALUE; 
        	Vertex prev = null; 
        	for (Vertex vertex : path){
        		text += vertex.getID() + "-->"; 
        		if (prev!=null){
        			Edge edge = G.findEdge(prev, vertex); 
        			if (edge != null){
        				bandWidth = Math.min(bandWidth, edge.getBandwidth()); 

        			}
        		}
        		prev = vertex; 
        	}
        	text += bandWidth; 
        	if (bandWidth > maxBandwidth[0]){
        		maxBandwidth [0] = bandWidth; 
        		bestPath [0] = text; 
        	}
        }
        // consider all neighbors that would continue path with repeating a node
        else {
            for (Vertex w : G.adjacentTo(v)) {
                if (!onPath.contains(w)) enumerate(G, w, t, maxBandwidth, bestPath);
            }
        }

        // done exploring from v, so remove from path
        path.pop();
        onPath.remove(v);
    }
}