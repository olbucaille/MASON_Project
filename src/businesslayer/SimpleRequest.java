package businesslayer;

import agents.BotX;

public class SimpleRequest extends Request {

	public SimpleRequest(BotX botX) {
		super(botX);
	type = StringProvider.TYPESIMPLEREQUEST;
	requester = botX;
	}

}
