package businesslayer;

import agents.BotX;
import agents.BotY;

public class Request {

	public String type; 
	public String Status; 
	public BotX requester;
	public BotY Handler;
	public BotX provider;
	
	public Request(BotX requester ,BotY handler)
	{
		type = StringProvider.TYPENORMALREQUEST;
		this.requester = requester;
		this.Handler = handler;
		Status = StringProvider.STATUS_DATAASKED;
		
	}

	public Request(BotX botX) {
		type = StringProvider.TYPENORMALREQUEST;
		this.requester = botX;

		Status = StringProvider.STATUS_DATAASKED;
	}
	
}
