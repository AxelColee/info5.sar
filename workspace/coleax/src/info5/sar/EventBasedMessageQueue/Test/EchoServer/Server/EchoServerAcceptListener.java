package info5.sar.EventBasedMessageQueue.Test.EchoServer.Server;

import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoServerAcceptListener implements IAcceptListener {

	private IQueueBroker _broker;
	private int _nbClient;
	private int _counter;
	
	 public EchoServerAcceptListener(IQueueBroker broker, int nbClient) {
		_broker = broker;
		_counter = 0;
		_nbClient = nbClient;
	}

	@Override
	public void accepted(IMessageQueue queue) {
		
		_counter++;
		
		queue.setListener(new EchoServerMessageListener(queue, _nbClient));
		
		if(_counter >= _nbClient) {
			_broker.unbind(80);
		}
	}

}
