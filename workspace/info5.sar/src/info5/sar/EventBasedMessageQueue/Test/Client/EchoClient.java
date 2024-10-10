package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.Task;

public class EchoClient implements Runnable{
	
	private QueueBroker _broker;
	
	public EchoClient(QueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		_broker.connect("toto", 80, new EchoClientConnectListener());		
	}
}
