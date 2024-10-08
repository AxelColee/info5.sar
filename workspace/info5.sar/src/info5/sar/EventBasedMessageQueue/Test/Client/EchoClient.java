package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.Task;

public class EchoClient extends Task{
	
	static Runnable getEchoClientRunnable() {
		return () ->{
			
			EchoClient client = (EchoClient) EchoClient.getTask();
			
			QueueBroker broker = client.getBroker();
			
			broker.connect("serverBroker", 80, new EchoClientConnectListener());
		};
		
	}

	public EchoClient(QueueBroker broker) {
		super(broker, EchoClient.getEchoClientRunnable());
	}
}
