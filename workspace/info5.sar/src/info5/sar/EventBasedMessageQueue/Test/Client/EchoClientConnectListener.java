package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.ConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClientConnectListener extends ConnectListener {
	
	private Message _msg;

	public EchoClientConnectListener(MessageListener messageListener, Message msg) {
		super(messageListener);
		_msg = msg;
	}

	@Override
	public void connected(IMessageQueue queue) {
		
		queue.setListener(getMessageListener());
		
		new Task().post(() -> queue.send(_msg));

	}

	@Override
	public void refused() {
		System.err.println("Connection refused");
	}

}
