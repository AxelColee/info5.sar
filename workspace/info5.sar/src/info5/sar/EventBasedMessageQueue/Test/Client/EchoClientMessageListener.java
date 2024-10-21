package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoClientMessageListener implements IMessageQueue.IMessageListener {
	
	private IMessageQueue _queue;
	private Message _message;
	
	public EchoClientMessageListener(IMessageQueue queue, Message msg) {
		_queue = queue;
		_message = msg;
	}

	@Override
	public void received(byte[] bytes) {

		//Tests
		for(int i = 0; i < _message.getLength(); i++){
			assert(bytes[i] == _message.getByteAt(i)) : "Data recieved different from the one sent : " + i;
		}	
		
		_queue.close();

		
	}

	@Override
	public void closed() {
		
		assert(_queue != null) : "Client Queue not initialized";
		assert(_queue.closed() == true) : "Client Queue not disconnected";
		
		System.out.println("Client passed");
	}

	@Override
	public void sent(Message message) {
		
	}

}
