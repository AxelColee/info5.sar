package info5.sar.EventBasedMessageQueue.Test.Client;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;

public class EchoClientMessageListener implements MessageListener{
	
	private MessageQueue _queue;
	private Message _message;
	private static int cpt = 0;
	
	public EchoClientMessageListener(MessageQueue queue, Message msg) {
		_queue = queue;
		_message = msg;
	}

	@Override
	public void received(byte[] bytes) {
		//_queue.close();
		
		//Tests
		for(int i = 0; i < _message.getLength(); i++){
			assert(bytes[i] == _message.getByteAt(i)) : "Data recieved different from the one sent : " + i;
		}	

		assert(_queue != null) : "Client Queue not initialized";
		assert(_queue.closed() == true) : "Client Queue not disconnected";
		
		System.out.println("Client passed");
		
		if(cpt++ >= 2) {
			EventPump.getInstance().stopPump();
		}
	}

	@Override
	public void closed() {
		_queue.close();
	}

	@Override
	public void sent(Message message) {
		
	}

}
