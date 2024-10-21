package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class EchoClient implements Runnable {
	
private IQueueBroker _broker;
	
	public EchoClient(IQueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		_broker.connect("serverBroker", 80, new EchoClientConnectListener());		
	}

}
