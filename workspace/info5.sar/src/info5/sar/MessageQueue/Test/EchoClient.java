package info5.sar.MessageQueue.Test;

import info5.sar.Channel.Abstract.Broker;
import info5.sar.Channel.Abstract.Channel;
import info5.sar.Channel.Exception.DisconnectedException;
import info5.sar.Channel.Impl.TaskImpl;
import info5.sar.MessageQueue.Abstract.MessageQueue;
import info5.sar.MessageQueue.Abstract.QueueBroker;
import info5.sar.MessageQueue.Abstract.Task;

public class EchoClient extends Task{
	
	static Runnable getEchoClientRunnable() {
		return () ->{
			EchoClient client = (EchoClient) EchoClient.getTask();
			
			QueueBroker broker = client.getQueueBroker();
			
			String string = "Ceci est un message";
			byte[] message = string.getBytes();
	
			MessageQueue messageQueue = broker.connect("toto", 80);
			
			messageQueue.send(message, 0, message.length);
			
			byte[] response = messageQueue.receive();
	
			messageQueue.close();
	
	
			//Tests
			for(int i = 0; i < message.length; i++){
				assert(response[i] == message[i]) : "Data recieved different from the one sent : " + i;
			}	
	
			assert(messageQueue != null) : "Client Channel not initialized";
			assert(messageQueue.closed() == true) : "Client Channel not disconnected";
		};
		
	}

	EchoClient(Broker b) {
		super(b, EchoClient.getEchoClientRunnable() );
	}
	
	public EchoClient(QueueBroker b) {
		super(b, EchoClient.getEchoClientRunnable());
	}

	@Override
	protected Broker getBroker() {
		return null;
	}

	@Override
	protected QueueBroker getQueueBroker() {
		return null;
	}
	
	

}
