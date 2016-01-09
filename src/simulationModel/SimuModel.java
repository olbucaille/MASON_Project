package simulationModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import optimum_position.DijkstraAlgorithm;
import optimum_position.Edge;
import optimum_position.Graph;
import optimum_position.Groupe;
import optimum_position.Vertex;
import agents.BotEnergyBaseStation;
import agents.BotX;
import agents.BotY;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;


/**
 * 
 * @author olbucaille
 *
 *
 *this simulation modelize : 
 *
 *fixed X and moving Y
 *All agents knows everything and are linked by a graph/network
 *
 */
public class SimuModel extends SimState {

	public Continuous2D yard = new Continuous2D(1.0,100,100);
	public double randomMultiplier = 0.1;
	public Network AllBotNetwork = new Network(false);
	
	private List<Vertex> nodes;
	private List<Edge> edges;
	
	private List <Groupe> groupList = new ArrayList <Groupe> ();


	//sum of all reuquest DONE 
	public static long NumberOfRequestTreated = 0;

	// a request has been raised but no ones cares
	public static long NumberOfRequestPending = 0;

	//currently managed -> a bot is taking care of the request but not yet finished
	public static long NumberOfRequestCurrentlyManaged = 0;

	//allows to get a percentage
	public double getNumberOfRequestTreatedOverTime(){
		if (this.schedule.getSteps()!= 0 )
		{

			return (float)NumberOfRequestTreated/this.schedule.getSteps();
		}
		return 0;}

	//num of Bots 
	public int numBotX = 10;
	public int numBotY = numBotX/2;
	public int numBotEnergyBaseStation = 2;
	
	public int nbNodeInGroupe = numBotX/numBotY;


	public SimuModel(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}



	// general ordonanceur
	public void start()
	{
		super.start();

		// clear the yard
		yard.clear();

		//clear the whole network
		AllBotNetwork.clear();

		nodes = new ArrayList<Vertex>();
	    edges = new ArrayList<Edge>();
	    

	    
		// add some BotsX to the yard
		for(int i = 0; i < numBotX; i++)
		{
			BotX botX = new BotX();
			Double2D pos = new Double2D(yard.getWidth() *  random.nextDouble() ,
					yard.getHeight() *  random.nextDouble() );
			yard.setObjectLocation(botX,pos);
			schedule.scheduleRepeating(botX);
			AllBotNetwork.addNode(botX);
			
			//creation des vertex du graph
			Vertex location = new Vertex("Node_"+i, "Node_" + i,pos);
		    nodes.add(location);
		    //initialisation
		}

	    
	    //create all edges
		int count = 0;
		int distance=0;
	    
	    for (int i = 0; i < numBotX; i++) {
	    	for (int j = 0; j < numBotX; j++) {
	    		distance = getDistance(nodes.get(i),nodes.get(j));
	    		addLane("Edge_"+count, i, j,distance );
	    		count++;
	    	}
	    }
	    // Lets check from location Loc_1 to Loc_10
	    Graph graph = new Graph(nodes, edges);
	    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

	    
	    
	    //on commence au vertex 0
	    Vertex next;
	    Vertex previous;
	    dijkstra.execute(nodes.get(0));
	    previous = nodes.get(0);
	    
	    Groupe currentGroup = new Groupe() ;
	    
	    currentGroup.addVertexOfGroupe(previous);
	    System.out.println(numBotY);
	    for (int i = 1; i < numBotX+1; i++) {
	    	//le plus proche voisin non visité
	    	next = null;
		    next = dijkstra.getNearestNeighbor(previous);
		    
		   if (i%nbNodeInGroupe!=0){
	    		// on ajoute le vertex dans le groupe 
			   currentGroup.addVertexOfGroupe(next);
			   System.out.println(next);
			   
		   }else{
			   //nouveau groupe et ajout du nouveaux vertex
			   groupList.add(currentGroup);
			   currentGroup = null;
			   currentGroup = new Groupe();
			   System.out.println("new groupe" + i);
			   currentGroup.addVertexOfGroupe(next);
			   System.out.println(next);
		   }
		   	previous = next;
		   	
	    }


	// add some BotsY to the yard
			for(int i = 0; i < numBotY; i++)
			{
				BotY botY = new BotY();
				botY.setDefautPos( groupList.get(i).getBarycentre());
				yard.setObjectLocation(botY, groupList.get(i).getBarycentre());			
				schedule.scheduleRepeating(botY);
				AllBotNetwork.addNode(botY);
			}

			// add some BotsEnergyBaseStation to the yard
			for(int i = 0; i < numBotEnergyBaseStation; i++)
			{
				BotEnergyBaseStation botEBS = new BotEnergyBaseStation();
				yard.setObjectLocation(botEBS,
						new Double2D(yard.getWidth() *  random.nextDouble() ,
								yard.getHeight() *  random.nextDouble() ));
				schedule.scheduleRepeating(botEBS);
				AllBotNetwork.addNode(botEBS);
			}

		}


	private void addLane(String laneId, int sourceLocNo, int destLocNo,
	      int duration) {
	    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
	    edges.add(lane);
	  }
	
	public int getDistance(Vertex v1 , Vertex v2){
		Double2D v1_2D = v1.getPos();
		Double2D v2_2D = v2.getPos();
		
		double distance = Math.sqrt(Math.pow((v2_2D.x-v1_2D.x),2)+Math.pow((v2_2D.y-v1_2D.y),2));
		return (int) distance;
	}

	public int getNumBotEnergyBaseStation() {
		return numBotEnergyBaseStation;
	}

	public void setNumBotEnergyBaseStation(int numBotEnergyBaseStation) {
		this.numBotEnergyBaseStation = numBotEnergyBaseStation;
	}

	public int getNumBotX() {
		return numBotX;
	}

	public void setNumBotX(int numBotX) {
		this.numBotX = numBotX;
	}

	public int getNumBotY() {
		return numBotY;
	}

	public void setNumBotY(int numBotY) {
		this.numBotY = numBotY;
	}

	public long getNumberOfRequestTreated() {
		return NumberOfRequestTreated;
	}

	public long getNumberOfRequestPending() {
		return NumberOfRequestPending;
	}

	public long getNumberOfRequestCurrentlyManaged() {
		return NumberOfRequestCurrentlyManaged;
	}

}
