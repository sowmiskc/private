//changes to the code provided by the textbook were made to accommodate for the specifics of this assignment
//Graph.java uses these setters and getters
/**
 *  The {@code Edge} class represents a weighted edge in an 
 *  {@link EdgeWeightedGraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the edge and
 *  the weight. The natural order for this data type is by
 *  ascending order of weight.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Edge implements Comparable<Edge>{
	//adjustments for project specifics
	private final int c_speed = 230000000; 
	private final int f_speed = 200000000; 
	private String Material; 
	private int Bandwidth; 
	private int Length; 
	private Vertex Source; 
	private Vertex Destination; 
	private double TimeToTravel; 

	public Edge(String material, int bandwidth, int length, Vertex destination, Vertex source){
		Material = material;
		Bandwidth = bandwidth;
		Length = length;
		Destination = destination;
		Source = source;

		if(Material.equals("copper")){
			TimeToTravel = ((double) 1/c_speed) * Length * Math.pow(10, 9); 
		} else if(Material.equals("optical")){
			TimeToTravel = ((double) 1/f_speed) * Length * Math.pow(10, 9); 
		}
	}
//setter methods
	public void setMaterial(String newMaterial){
		Material = newMaterial;
	}

	public void setBandwidth(int newBandwidth){
		Bandwidth = newBandwidth;
	}

	public void setLength(int newLength){
		Length = newLength;
	}

	public void setDestination(Vertex newDestination){
		Destination = newDestination;
	}

	public String getMaterial(){
		return Material;
	}
//getter methods
	public int getBandwidth(){
		return Bandwidth;
	}

	public int getLength(){
		return Length;
	}

	public Vertex getDestination(){
		return Destination;
	}

	public double getTimeToTravel(){
		return TimeToTravel;
	}

	public Vertex getSource(){
		return Source;
	}
//comparing the edges
	public int compareTo(Edge otherEdge){
		if(TimeToTravel > otherEdge.getTimeToTravel()){
			return 1;
		} else if(TimeToTravel == otherEdge.getTimeToTravel()){
			return 0;
		} else{
			return -1;
		}
	}
}