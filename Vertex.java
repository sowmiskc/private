//this class is used to acknowledge the contents of the file being read when a user chooses 
//a certain menu item from the driver program NetworkAnalysis.java
import java.util.*;
public class Vertex{
	private LinkedList<Edge> Connections;
	private int ID; 
//id is used to identify the vertex the user would like to look at
	public Vertex(int id){
		ID = id;
		Connections = new LinkedList<Edge>();
	}
//used for the connections between vertices
	public LinkedList<Edge> getConnections(){
		return Connections;
	}
	//gets the id user wants for what is listed in the data file
	public int getID(){
		return ID;
	}
}