package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker.IConnectListener;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClientConnectListener implements IConnectListener {

	@Override
	public void connected(IMessageQueue queue) {
		Message msg = new Message(UUID.randomUUID().toString().repeat(10).getBytes());
		
		queue.setListener(new EchoClientMessageListener(queue, msg));
		
		new Task().post(() -> queue.send(msg));

	}

	@Override
	public void refused() {
		System.err.println("Connection refused");
	}

}
