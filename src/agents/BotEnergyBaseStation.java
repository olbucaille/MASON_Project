package agents;

import java.awt.Color;
import java.awt.Paint;

import businesslayer.StringProvider;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;
import simulationModel.SimuModel;

public class BotEnergyBaseStation extends Bot {

	//used to paint on IHM the bot 
	public Paint getColor() {

		return new Color(0,0,255); 

	} 
	
	@Override
	public void step(SimState state) {
	
		SimuModel SM = (SimuModel) state;
		Bag all =SM.yard.getAllObjects();
		Double2D me = SM.yard.getObjectLocation(this);
		int j=0;
		while (j<all.numObjs)
		{
		if(all.get(j).getClass().equals(BotY.class))
		{
			if(me.distance(SM.yard.getObjectLocation(all.get(j)))<3 )
			{
				BotY botToRefill = (BotY) all.get(j);
				botToRefill.Energy= botToRefill.minEnergy;
				botToRefill.isRefulling = false;
			}
		}
		j++;
		}
	
	}


}
