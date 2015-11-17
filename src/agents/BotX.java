package agents;

import java.awt.Color;
import java.awt.Paint;

import businesslayer.StringProvider;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
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
public class BotX extends Bot {
		
	public String Status="";
	public Boolean IsManaged;
	Double ChanceToMakeARequest=0.005;

	SimuModel SM;

	public BotX() {
		Status = StringProvider.STATUS_STANDBY;
		IsManaged = false;
		PrimeIdentifier=StringProvider.PRIMEIDENTIFIERX;
	}

	@Override
	public void step(SimState state) {
		SM=(SimuModel) state;
		if(Status.equals(StringProvider.STATUS_STANDBY))
		{	if(DoIRequest())
			MakeARequest();
		}
		else
		{
			Double2D me = SM.yard.getObjectLocation(this);
			Bag out = SM.AllBotNetwork.getEdges(this, null);
			if(out != null && out.size()>=1 && !Status.equals(StringProvider.STRING_REQUESTINGANDWAITINGANSWER))
			{
				Edge e = (Edge)(out.get(0));
				
				Double2D him = SM.yard.getObjectLocation(e.getOtherNode(this));
				if(him != null && me.distance(him)<5 && !((BotY)SM.yard.getObjectsAtLocation(him).get(0)).haveData && e.info.equals("coming"))//connexion etablie ( dangereux à terme d'identifier un objet par sa position, à revoir, passer par id, de manière generale revoir la gestion des etapes, commenter et organiser
				{
					//demande une info d'un BOTX random
					Bag list = SM.yard.getAllObjects();
					int ToRequest = -1;
					while(!(ToRequest != -1 &&SM.yard.getAllObjects().get(ToRequest).getClass().equals(BotX.class)))
					{
					//	System.out.println(SM.random.nextInt()%(list.size()-1));
					ToRequest =Math.abs(SM.random.nextInt()%(list.size()-1));
					}
					SM.AllBotNetwork.addEdge(e.getOtherNode(this),SM.yard.getAllObjects().get(ToRequest),"target");
					Status = StringProvider.STRING_REQUESTINGANDWAITINGANSWER;
				}
			}
		}


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

	public void feeded() {
	IsManaged = false;
	Status = StringProvider.STATUS_STANDBY;
	}
}
