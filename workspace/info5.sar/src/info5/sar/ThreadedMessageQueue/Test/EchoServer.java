package info5.sar.ThreadedMessageQueue.Test;

import info5.sar.ThreadedChannel.Abstract.Broker;
import info5.sar.ThreadedMessageQueue.Abstract.MessageQueue;
import info5.sar.ThreadedMessageQueue.Abstract.QueueBroker;
import info5.sar.ThreadedMessageQueue.Impl.Task;

public class EchoServer extends Task{
	
	static Runnable getEchoServerRunnable() {
		return () ->{
			EchoServer client = (EchoServer) EchoServer.getTask();
			
			QueueBroker broker = client.getQueueBroker();
						
			for(int i = 0; i < 3; i++) {
				
				MessageQueue messageQueue = broker.accept(80);
				
				byte[] message = messageQueue.receive();

				messageQueue.send(message, 0, message.length);
						
				messageQueue.close();
		
				assert(messageQueue != null) : "Client Channel not initialized";
				assert(messageQueue.closed() == true) : "Client Channel not disconnected";
				
			}
		};
		
	}

	EchoServer(Broker b) {
		super(b, EchoServer.getEchoServerRunnable() );
	}
	
	public EchoServer(QueueBroker b) {
		super(b, EchoServer.getEchoServerRunnable());
	}
}