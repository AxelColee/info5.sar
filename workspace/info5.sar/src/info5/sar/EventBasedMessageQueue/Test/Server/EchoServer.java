package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoServer implements Runnable{
private IQueueBroker _broker;
	
	public EchoServer(IQueueBroker broker) {
		_broker = broker;
	}


	@Override
	public void run() {
		new Task().post( () -> _broker.bind(80, new EchoServerAcceptListener(_broker)));
		
	}

}
