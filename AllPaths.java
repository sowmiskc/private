//https://algs4.cs.princeton.edu/41graph/
//Referenced code from the Algorithms book, link above

import java.util.*;

public class AllPaths{

    private Stack<Vertex> path  = new Stack<Vertex>();   
    private ArrayList<Vertex> onPath  = new ArrayList<Vertex>();     

    public AllPaths(Graph G, Vertex s, Vertex t, String[] bestPath ) {
        enumerate(G, s, t, new int [1], bestPath);
    }

    // depth first search
    private void enumerate(Graph G, Vertex v, Vertex t, int [] maxBandwidth, String [] bestPath) {

        // adding v to the path
        path.push(v);
        onPath.add(v);

        // finding path
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
        //look at neighbors in path
        else {
            for (Vertex w : G.adjacentTo(v)) {
                if (!onPath.contains(w)) enumerate(G, w, t, maxBandwidth, bestPath);
            }
        }

        //all have been visited so we can remove it
        path.pop();
        onPath.remove(v);
    }
}