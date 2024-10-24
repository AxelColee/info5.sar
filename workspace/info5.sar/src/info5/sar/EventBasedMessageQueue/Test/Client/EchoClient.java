package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedMessageQueue.Abstract.ConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClient implements Runnable {
	
private IQueueBroker _broker;
	
	public EchoClient(IQueueBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		Message msg = new Message(UUID.randomUUID().toString().repeat(10).getBytes());
				
		ConnectListener connectListener = new EchoClientConnectListener(msg);
		
		_broker.connect("serverBroker", 80, connectListener);		
	}

}
