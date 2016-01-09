package optimum_position;

public class Edge  {
	private final String id; 
	private final Vertex source;
	private final Vertex destination;
	private final int distance; 
	  
	public Edge(String id, Vertex source, Vertex destination, int distance) {
		this.id = id;
	    this.source = source;
	    this.destination = destination;
	    this.distance = distance;
	}
	  
	public String getId() {
	    return id;
	}
	public Vertex getDestination() {
	    return destination;
	}
	
	public Vertex getSource() {
	    return source;
	}
	public int getDistance() {
	    return distance;
	}
	  
	@Override
	public String toString() {
	    return source + " " + destination+"\n";
	}	  
} 
	
