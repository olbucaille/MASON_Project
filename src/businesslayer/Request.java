package businesslayer;

import agents.BotX;
import agents.BotY;

public class Request {

	public String Status; 
	public BotX requester;
	public BotY Handler;
	public BotX provider;
	
	public Request(BotX requester ,BotY handler)
	{
		this.requester = requester;
		this.Handler = handler;
		Status = StringProvider.STATUS_DATAASKED;
		
	}

	public Request(BotX botX) {
		this.requester = requester;

		Status = StringProvider.STATUS_DATAASKED;
	}
	
}
