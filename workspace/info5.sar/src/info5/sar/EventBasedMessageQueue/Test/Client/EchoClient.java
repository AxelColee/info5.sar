package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedMessageQueue.Abstract.ConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClient implements Runnable {
	
	private IQueueBroker _broker;
	private int _nbMessage;
	
	public EchoClient(IQueueBroker broker, int nbMessage) {
		_broker = broker;
		_nbMessage = nbMessage;
	}

	@Override
	public void run() {
				
		ConnectListener connectListener = new EchoClientConnectListener(_nbMessage);
		
		_broker.connect("serverBroker", 80, connectListener);	
	}

}
