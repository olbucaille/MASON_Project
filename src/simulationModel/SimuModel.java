package simulationModel;

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
	
	
	public int numBotX = 5;
	public int numBotY = 5;


	public SimuModel(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}


	public void start()
	{
		super.start();

		// clear the yard
		yard.clear();

		//clear the whole network
		AllBotNetwork.clear();
		
		// add some BotsX to the yard
		for(int i = 0; i < numBotX; i++)
		{
			BotX botX = new BotX();
			yard.setObjectLocation(botX,
					new Double2D(yard.getWidth() *  random.nextDouble() ,
							yard.getHeight() *  random.nextDouble() ));
			schedule.scheduleRepeating(botX);
			AllBotNetwork.addNode(botX);
		}

		// add some BotsY to the yard
		for(int i = 0; i < numBotY; i++)
		{
			BotY botY = new BotY();
			yard.setObjectLocation(botY,
					new Double2D(yard.getWidth() *  random.nextDouble() ,
							yard.getHeight() *  random.nextDouble() ));
			schedule.scheduleRepeating(botY);
			AllBotNetwork.addNode(botY);
		}

	}


}
