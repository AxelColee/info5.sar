package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.ConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Test.Server.EchoServerMessageListener;

public class EchoClientConnectListener extends ConnectListener {
	
	private Message _msg;

	public EchoClientConnectListener(Message msg) {
		_msg = msg;
	}

	@Override
	public void connected(IMessageQueue queue) {
		
		queue.setListener(new EchoClientMessageListener(queue, _msg));
		
		new Task().post(() -> queue.send(_msg));

	}

	@Override
	public void refused() {
		System.err.println("Connection refused");
	}

}
