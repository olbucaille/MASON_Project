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
public class BotY implements Steppable {

	public String Status="";
	public boolean isOccupied = false;
	SimuModel SM;
	public BotY() {
		Status = StringProvider.STATUS_WAITING;
	}

	@Override
	public void step(SimState state) {
		SM = (SimuModel) state;
		Continuous2D yard = SM.yard;
		Double2D me = SM.yard.getObjectLocation(this);
		MutableDouble2D sumForces = new MutableDouble2D();

		if(isOccupied)
			return;
		Bag AllBots = SM.AllBotNetwork.getAllNodes();
		for(int i = 0; i < AllBots.size(); i++)
		{
			Object bot = AllBots.get(i);

			if(bot.getClass().equals(BotX.class)) //if it is a BOTX
			{
				BotX b= (BotX) bot;
				if(b.Status.equals(StringProvider.STATUS_REQUESTING)&&b.IsManaged == false)//And requestind (add after isCaredbySomeone)
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
						//PB D'AFFECTATION, A reflechir....
						
						if(nearest!= null && nearest.equals(this))
						{
							SM.AllBotNetwork.addEdge(this, b, "coming");
							b.IsManaged= true;
							this.isOccupied = true;
							this.Status = StringProvider.STATUS_RESPONDING;
							break;
						}
					}
				}
			}


			
		}
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

		SM.yard.setObjectLocation(this, new Double2D(sumForces));


	}

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
