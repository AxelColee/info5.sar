package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;

public class EchoServer implements Runnable{
	
private QueueBroker _broker;
	
	public EchoServer(QueueBroker broker) {
		_broker = broker;
	}


	@Override
	public void run() {
		_broker.bind(80, new EchoServerAcceptListener(_broker));
		
	}
}
