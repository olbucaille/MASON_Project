package agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;
import simulationModel.SimuModel;

public class BotY implements Steppable {

	@Override
	public void step(SimState state) {
		SimuModel SM = (SimuModel) state;
		Continuous2D yard = SM.yard;
		Double2D me = SM.yard.getObjectLocation(this);
		MutableDouble2D sumForces = new MutableDouble2D();




		
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

}
