package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;

public class EchoServerAcceptListener implements AcceptListener{
	
	private QueueBroker _broker;
	private int cpt = 0;
	
	 public EchoServerAcceptListener(QueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void accepted(MessageQueue queue) {
		
		queue.setListener(new EchoServerMessageListener(queue));
		
		if(cpt++ >= 2) {
			//_broker.unbind(80);
		}
	}
	

}
