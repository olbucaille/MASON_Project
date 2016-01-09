package optimum_position;

import java.util.ArrayList;
import java.util.List;

import sim.util.Double2D;

public class Groupe {
	
	private List <Vertex> vertexOfGroupe;
	
	public Groupe( List <Vertex> vertexOfGroupe){
		this.vertexOfGroupe = vertexOfGroupe;
	}
	
	public Groupe(){
		this.vertexOfGroupe = new ArrayList <Vertex>();
	}

	public List <Vertex> getVertexOfGroupe() {
		return vertexOfGroupe;
	}

	public void setVertexOfGroupe(List <Vertex> vertexOfGroupe) {
		this.vertexOfGroupe = vertexOfGroupe;
	}
	public void addVertexOfGroupe(Vertex vertex) {
		this.vertexOfGroupe.add( vertex );
	}
	
	public int getNbNodeInGroupe(){
		return vertexOfGroupe.size();
	}
	
	
	public Double2D getBarycentre(){
		
		double x = 0; 
		double y = 0; 
		
		for( Vertex node : this.vertexOfGroupe){
			System.out.println(node);
			x=x+node.getPos().x;
			y=y+node.getPos().y;
		}
		
		return new Double2D(x/this.vertexOfGroupe.size(),y/this.vertexOfGroupe.size());
	}
	
}
