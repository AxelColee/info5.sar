package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.AcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoServerAcceptListener extends AcceptListener {

	private IQueueBroker _broker;
	private int _nbClient;
	private int _nbMessagePerClient;
	private int _counter;
	
	 public EchoServerAcceptListener(IQueueBroker broker, int nbClient, int nbMessagePerClient) {
		_broker = broker;
		_counter = 0;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void accepted(IMessageQueue queue) {
		
		_counter++;
		
		queue.setListener(new EchoServerMessageListener(queue, _nbClient));
		
		if(_counter >= _nbClient) {
			new Task().post(() -> _broker.unbind(80));
		}
	}

}
