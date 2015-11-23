package agents;

import java.awt.Paint;

import sim.engine.SimState;
import sim.engine.Steppable;

public abstract class Bot  implements Steppable{

	public String PrimeIdentifier="UNKNOWN";
	
	@Override
	public void step(SimState arg0) {
		
	}
	
	public Paint getColor() {
		return null;
	
	}

}
