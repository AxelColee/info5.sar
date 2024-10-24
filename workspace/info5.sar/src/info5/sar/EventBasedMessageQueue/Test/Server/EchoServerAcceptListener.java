package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.AcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoServerAcceptListener extends AcceptListener {

	private IQueueBroker _broker;
	private int cpt = 0;
	
	 public EchoServerAcceptListener( IQueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void accepted(IMessageQueue queue) {
		
		queue.setListener(new EchoServerMessageListener(queue));
		
		if(cpt++ >= 2) {
			new Task().post(() -> _broker.unbind(80));
		}
	}

}
