package agents;

import java.awt.Color;
import java.awt.Paint;

import businesslayer.Request;
import businesslayer.SimpleRequest;
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

	//representing its current status, it is updated either by itself or not.
	//it can take the following values : 
	// STANDBY : -> doing nothing
	// REQUESTING : -> requesting, no bot came to handle it yet
	//STRING_REQUESTINGANDWAITINGANSWER : -> a bot came, it wait the answer to close the request
	public String Status="";

	// reprensenting if a botY is handling the request yet 
	public Boolean IsManaged;

	// describe the percentage to make a request at each step.
	Double ChanceToMakeARequest=0.005;

	Request request;
	SimuModel SM;
	public Double getChance(){return ChanceToMakeARequest;}
	public BotX() {
		Status = StringProvider.STATUS_STANDBY;
		IsManaged = false;
		PrimeIdentifier=StringProvider.PRIMEIDENTIFIERX;
	}

	@Override
	public void step(SimState state) {
		SM=(SimuModel) state;
		//if do nothinf, then check if it have to make a request
		if(Status.equals(StringProvider.STATUS_STANDBY))
		{	if(DoIRequest())
			MakeARequest();
		}
		else
		{
			Double2D me = SM.yard.getObjectLocation(this);

			if(!Status.equals(StringProvider.STRING_REQUESTINGANDWAITINGANSWER))
			{


				Double2D him = SM.yard.getObjectLocation(request.Handler);
				// if distance <5 and bot approching is owning a data ( so coming back) and is on the node with info "coming")
				if(him != null && me.distance(him)<5 && !(request.Handler.haveData))//connexion etablie (FIXEDDDDDDD, I GUESS... dangereux à terme d'identifier un objet par sa position, à revoir, passer par id, de manière generale revoir la gestion des etapes, commenter et organiser
				{
					//demande une info d'un BOTX random
					Bag list = SM.yard.getAllObjects();
					int ToRequest = -1;
					while(!(ToRequest != -1 &&SM.yard.getAllObjects().get(ToRequest).getClass().equals(BotX.class)))
					{
						//	System.out.println(SM.random.nextInt()%(list.size()-1));
						ToRequest =Math.abs(SM.random.nextInt()%(list.size()-1));
					}
					request.provider = (BotX) SM.yard.getAllObjects().get(ToRequest);
					SM.AllBotNetwork.addEdge(request.Handler,request.provider,"target");
					Status = StringProvider.STRING_REQUESTINGANDWAITINGANSWER;
					SimuModel.NumberOfRequestPending--;
					SimuModel.NumberOfRequestCurrentlyManaged++;
				}
			}
		}


	}

	private void MakeARequest() {

		if( SM.random.nextBoolean(0.5))
			request = new Request(this);
		else
			request = new SimpleRequest(this);
		Status = StringProvider.STATUS_REQUESTING;
		SimuModel.NumberOfRequestPending++;

	}

	private boolean DoIRequest() {
		return SM.random.nextBoolean(ChanceToMakeARequest);

	}
	//used to paint on IHM the bot 
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
		request = null;
		IsManaged = false;
		Status = StringProvider.STATUS_STANDBY;
		SimuModel.NumberOfRequestCurrentlyManaged--;
		SimuModel.NumberOfRequestTreated++;
	}
}
