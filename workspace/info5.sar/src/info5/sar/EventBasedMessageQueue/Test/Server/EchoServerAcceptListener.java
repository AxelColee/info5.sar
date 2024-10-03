package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;

public class EchoServerAcceptListener implements AcceptListener{

	@Override
	public void accepted(MessageQueue queue) {
		
		queue.setListener(new EchoServerMessageListener(queue));
	}

}
