package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoServer implements Runnable{
	
	private IQueueBroker _broker;
	private final int _nbClient;
	private final int _messagePerClient;
	
	public EchoServer(IQueueBroker broker, int nbClient, int messagePerClient) {
		_broker = broker;
		_nbClient = nbClient;
		_messagePerClient = messagePerClient;
	}


	@Override
	public void run() {
		new Task().post( () -> _broker.bind(80, new EchoServerAcceptListener(_broker, _nbClient, _messagePerClient)));
	}

}
