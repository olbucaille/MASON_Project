package agents;

import java.awt.Color;
import java.awt.Paint;

import businesslayer.StringProvider;
import sim.engine.SimState;
import sim.engine.Steppable;
import simulationModel.SimuModel;

/**
 * 
 * @author olbucaille
 *
 * BotX are the agents acting like a client 
 * 
 * in this version they are :
 * 
 * movable : false
 * requesting : false
 * communicatingY : false
 * communicatingX : false
 * owning Data    : false
 *  
 */
public class BotX implements Steppable{

	public String Status="";
	public Boolean IsManaged;
	Double ChanceToMakeARequest=0.001;
	
	SimuModel SM;
	
	public BotX() {
	Status = StringProvider.STATUS_STANDBY;
	IsManaged = false;
	}
	
	@Override
	public void step(SimState state) {
		SM=(SimuModel) state;
		if(Status.equals(StringProvider.STATUS_STANDBY))
			if(DoIRequest())
				MakeARequest();
		
	}
	
	private void MakeARequest() {
		
		
		Status = StringProvider.STATUS_REQUESTING;
		
	}

	private boolean DoIRequest() {
		return SM.random.nextBoolean(ChanceToMakeARequest);
		
	}

	public Paint getColor() {
		switch (Status)
		{
		  case StringProvider.STATUS_STANDBY:
			  return new Color(0,80,0); 
		  case StringProvider.STATUS_REQUESTING:
			  return new Color(255,51,0); 
			  
		  default:
			return new Color(0,0,204); 
		} 
	}
}
