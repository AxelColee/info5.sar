package info5.sar.MixedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.MixedMessageQueue.Abstract.MessageQueue;
import info5.sar.MixedMessageQueue.Abstract.QueueBroker.ConnectListener;
import info5.sar.MixedMessageQueue.Impl.Message;

public class EchoClientConnectListener implements ConnectListener{

	@Override
	public void connected(MessageQueue queue) {
		
		Message msg = new Message(UUID.randomUUID().toString().repeat(10).getBytes());
		
		queue.setListener(new EchoClientMessageListener(queue, msg));
		
		queue.send(msg);
	}

	@Override
	public void refused() {
		//Nothing to do here
	}

}
