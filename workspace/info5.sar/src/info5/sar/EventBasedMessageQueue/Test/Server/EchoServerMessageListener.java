package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;

public class EchoServerMessageListener implements MessageListener{
	
	private MessageQueue _queue;
	private static int cpt = 0;
	
	public EchoServerMessageListener(MessageQueue queue) {
		_queue = queue;
	}

	@Override
	public void received(byte[] bytes) {
		
		Task task = new Task();
		task.post(new Runnable() {
			
			@Override
			public void run() {
				_queue.send(new Message(bytes));
				
			}
		});
		
	}

	@Override
	public void closed() {
		_queue.close();
	}

	@Override
	public void sent(Message message) {
		_queue.close();
		
		assert(_queue != null) : "Server queue not initialized";
		assert(_queue.closed() == true) : "Server queue not disconnected";
		
		if(cpt++ >= 2) {
			System.out.println("Server passed");
		}

	}

}
