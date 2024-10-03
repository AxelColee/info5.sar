package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Abstract.Message;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;

public class EchoClientMessageListener implements MessageListener{
	
	private MessageQueue _queue;
	private Message _message;
	
	public EchoClientMessageListener(MessageQueue queue, Message msg) {
		_queue = queue;
		_message = msg;
	}

	@Override
	public void received(byte[] bytes) {
		_queue.close();
		
		//Tests
		for(int i = 0; i < _message.getLength(); i++){
			assert(bytes[i] == _message.getByteAt(i)) : "Data recieved different from the one sent : " + i;
		}	

		assert(_queue != null) : "Client Queue not initialized";
		assert(_queue.closed() == true) : "Client Queue not disconnected";
	}

	@Override
	public void closed() {
		_queue.close();
	}

	@Override
	public void sent(Message message) {
		//Is receiving
	}

}
