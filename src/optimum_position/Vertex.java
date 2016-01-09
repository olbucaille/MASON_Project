package optimum_position;

import sim.util.Double2D;


public class Vertex {
  final private String id;
  final private String name;
  final private Double2D pos;
  private boolean visited;
  
  
  public Vertex(String id, String name,Double2D pos) {
    this.id = id;
    this.name = name;
    this.pos=pos;
  }
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Vertex other = (Vertex) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
	  String str = "name : "+name+" posX : "+ pos.x+" posY :"+pos.y+"\n";
    return str;
  }
public Double2D getPos() {
	return pos;
}

  
} 
