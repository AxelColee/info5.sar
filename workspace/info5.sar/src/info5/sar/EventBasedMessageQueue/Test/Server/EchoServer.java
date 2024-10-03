package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;
import info5.sar.EventBasedMessageQueue.Abstract.Task;

public class EchoServer extends Task{
	
	static Runnable getEchoClientRunnable() {
		return () ->{
			EchoServer client = (EchoServer) EchoServer.getTask();
			
			QueueBroker broker = client.getBroker();
			
			broker.bind(80, new EchoServerAcceptListener());
		};
	}
	
	public EchoServer(QueueBroker broker) {
		super(broker, EchoServer.getEchoClientRunnable());
	}
	
}
