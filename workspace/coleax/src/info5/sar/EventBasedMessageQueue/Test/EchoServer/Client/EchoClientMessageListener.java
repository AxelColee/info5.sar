package info5.sar.EventBasedMessageQueue.Test.EchoServer.Client;

import java.util.List;

import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;

public class EchoClientMessageListener extends MessageListener {
	
	private List<byte[]> _messages;
	private int _counter;
	
	public EchoClientMessageListener(IMessageQueue queue, List<byte[]> messages) {
		super(queue);
		_messages = messages;
		_counter = 0;
	}

	@Override
	public void received(byte[] bytes) {

		byte[] byteArrayReceived = _messages.get(_counter);
		
		for(int i = 0; i < byteArrayReceived.length; i++){
			assert(bytes[i] == byteArrayReceived[i]) : "Data recieved different from the one sent : " + i;
		}	
		
		_queue.close();
		
		_counter++;

		
	}

	@Override
	public void closed() {
		
		assert(_queue != null) : "Client Queue not initialized";
		assert(_queue.closed() == true) : "Client Queue not disconnected";
		
		System.out.println("Client passed");
	}

	@Override
	public void sent(byte[] message) {
		//Nothing to do here
	}

}
