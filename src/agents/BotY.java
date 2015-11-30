package agents;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.event.SwingPropertyChangeSupport;

import businesslayer.StringProvider;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;
import simulationModel.SimuModel;


/**
 * 
 * @author olbucaille
 *
 * BotX are the agents acting like a client 
 * 
 * in this version they are :
 * 
 * movable : true
 * requesting : false
 * communicatingY : false
 * communicatingX : false
 *  
 */
public class BotY extends Bot {
	

	// inform on status as well as status on BotX
	// the differents values for BotY are : 
	// WAITING when BotY are waiting for a request to handle
	// RESPONDING when occupied to search answer or go to a new requester
	public String Status="";
	//is handling something
	public boolean isOccupied = false;
	//carry data
	public boolean haveData = false;
	SimuModel SM;
	
	//speed indicator
	double speedMultiplier = 0.1;

	 public Double getspeedMultiplier(){return speedMultiplier;}
	public BotY() {
		Status = StringProvider.STATUS_WAITING;
		PrimeIdentifier=StringProvider.PRIMEIDENTIFIERY;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void step(SimState state) {
		
		
		SM = (SimuModel) state;
		Continuous2D yard = SM.yard;
		Double2D me = SM.yard.getObjectLocation(this);
		MutableDouble2D sumForces = new MutableDouble2D();
		sumForces.addIn(me);
		if(isOccupied)
		{
			Bag out = SM.AllBotNetwork.getEdges(this, null);
			
			if(out!= null && out.size()>=1)
			{
				int i = 0;
				i = out.size()-1;
			Edge e = (Edge)(out.get(i));
			if(e!= null)
			{
					
			Double2D him = SM.yard.getObjectLocation(e.getOtherNode(this));
			//get the most recent node ( come or come back)
			if(him != null && me.distance(him)<5 && e.info.equals("target") )
			{
				haveData = true;
				SM.AllBotNetwork.removeEdge(e);
				e = (Edge)(out.get(i-1));
			}
			else if(him != null && me.distance(him)<5 && e.info.equals("coming") && haveData==true )
			{
				haveData=false;
				((BotX)SM.yard.getObjectsAtLocation(him).get(0)).feeded();// danger ! to confuse diff objects at diff locations
				Status = StringProvider.STATUS_WAITING;
				isOccupied = false;
				SM.AllBotNetwork.removeEdge(e);
				
				
			}
			if(isOccupied)
			sumForces.addIn(new Double2D((him.x-me.x )*speedMultiplier,	(him.y-me.y )*speedMultiplier));
			}
			}
			
				
		}
		else
		{
			
			sumForces.addIn(new Double2D((yard.width * 0.5 - me.x) *speedMultiplier,
					(yard.height * 0.5 - me.y) * speedMultiplier));
		Bag AllBots = SM.AllBotNetwork.getAllNodes();
		for(int i = 0; i < AllBots.size(); i++)
		{
			Object bot = AllBots.get(i);

			if(bot != null &&bot.getClass().equals(BotX.class)) //if it is a BOTX
			{
				BotX b= (BotX) bot;
				if(b.Status.equals(StringProvider.STATUS_REQUESTING)&&b.IsManaged == false)//And requestind 
				{
					Bag NearestNeighbors = SM.yard.getAllObjects();
					Double2D posb = SM.yard.getObjectLocation(b);
					BotY nearest=null;
					double mindist = 1000000;

					for(int j = 0; j < NearestNeighbors.size(); j++)
					{
						
					
						if(NearestNeighbors.get(j).getClass().equals(BotY.class))
						{
						Double2D posY = SM.yard.getObjectLocation(NearestNeighbors.get(j));
						if(posb.distance(posY)<mindist&&((BotY)NearestNeighbors.get(j)).isOccupied==false )
						{
							mindist = posb.distance(posY);
							nearest = (BotY) NearestNeighbors.get(j);
						}
						}
						
					}
						if(nearest!= null && nearest.equals(this))
						{
							SM.AllBotNetwork.addEdge(this, b, "coming");
							b.IsManaged= true;
							b.request.Handler = this;
							this.isOccupied = true;
							this.Status = StringProvider.STATUS_RESPONDING;
							break;
						}
					
				}
			}
			
		}

		// add a bit of randomness
					sumForces.addIn(new Double2D(SM.randomMultiplier * (SM.random.nextDouble() * 1.0 - 0.5),
							SM.randomMultiplier * (SM.random.nextDouble() * 1.0 - 0.5)));

					
					if(sumForces.getX()>SM.yard.getWidth())
						sumForces.setTo(new Double2D(SM.yard.getWidth(),sumForces.y));

					if(sumForces.getX()<0)
						sumForces.setTo(new Double2D(0,sumForces.y));

					if(sumForces.getY()>SM.yard.getHeight())
						sumForces.setTo(new Double2D(sumForces.x,SM.yard.getHeight()));

					if(sumForces.getY()<0)
						sumForces.setTo(new Double2D(0,SM.yard.getHeight()));
		}
		

		SM.yard.setObjectLocation(this, new Double2D(sumForces));


	}

	//used to paint on IHM the bot 
	public Paint getColor() {
		switch (Status)
		{
		case StringProvider.STATUS_WAITING:
			return new Color(0,190,255); 
		case StringProvider.STATUS_RESPONDING:
			return new Color(51,0,102); 
		default:
			return new Color(0,0,204); 
		} 
	}

}


/*ONLY RANDOM EVERYTIME, DOESNT WORK AT ALL
// add a bit of randomness
sumForces.addIn(new Double2D(SM.randomMultiplier * (SM.random.nextDouble() * 1.0 - 0.5),
		SM.randomMultiplier * (SM.random.nextDouble() * 1.0 - 0.5)));

sumForces.addIn(me);
if(sumForces.getX()>SM.yard.getWidth())
	sumForces.setTo(new Double2D(SM.yard.getWidth(),sumForces.y));

if(sumForces.getX()<0)
	sumForces.setTo(new Double2D(0,sumForces.y));

if(sumForces.getY()>SM.yard.getHeight())
	sumForces.setTo(new Double2D(sumForces.x,SM.yard.getHeight()));

if(sumForces.getY()<0)
	sumForces.setTo(new Double2D(0,SM.yard.getHeight()));
 */
