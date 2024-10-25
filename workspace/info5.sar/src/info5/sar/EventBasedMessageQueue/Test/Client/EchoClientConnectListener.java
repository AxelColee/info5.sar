package info5.sar.EventBasedMessageQueue.Test.Client;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.ConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClientConnectListener extends ConnectListener {
	
	private int _nbMessage;
	private List<Message> _messages;

	public EchoClientConnectListener(int nbMessage) {
		_nbMessage = nbMessage;
		_messages = new LinkedList<Message>();
	}

	@Override
	public void connected(IMessageQueue queue) {
		
		for(int i = 0; i < _nbMessage; i++) {
			_messages.add(new Message(UUID.randomUUID().toString().repeat(10).getBytes()));
		}
		
		queue.setListener(new EchoClientMessageListener(queue, _messages));
		
		for(int i = 0; i < _nbMessage; i++) {
			final int index = i;
			new Task().post(() -> queue.send(_messages.get(index)));
		}
		

	}

	@Override
	public void refused() {
		System.err.println("Connection refused");
	}

}
