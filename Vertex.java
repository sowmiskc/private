import java.util.*;

public class Vertex{
	private LinkedList<Edge> Connections;
	private int ID; 

	public Vertex(int id){
		ID = id;
		Connections = new LinkedList<Edge>();
	}
	
	public LinkedList<Edge> getConnections(){
		return Connections;
	}

	public int getID(){
		return ID;
	}
}