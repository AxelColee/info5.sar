package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.ConnectListener;

public class EchoClientConnectListener implements ConnectListener{

	@Override
	public void connected(MessageQueue queue) {
		
		Message msg = new Message("Hello from Client".getBytes(), 0, "Hello from Client".length());
		
		queue.setListener(new EchoClientMessageListener(queue, msg));
		
		queue.send(msg);
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connect Refused");
	}

}
