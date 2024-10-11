package info5.sar.MixedMessageQueue.Test.Client;

import info5.sar.MixedMessageQueue.Abstract.QueueBroker;
import info5.sar.MixedMessageQueue.Abstract.Task;

public class EchoClient implements Runnable{
	
	private QueueBroker _broker;
	
	public EchoClient(QueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		_broker.connect("serverBroker", 80, new EchoClientConnectListener());		
	}
}
