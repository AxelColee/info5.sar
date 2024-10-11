package info5.sar.MixedMessageQueue.Test.Server;

import info5.sar.MixedMessageQueue.Abstract.MessageQueue;
import info5.sar.MixedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.MixedMessageQueue.Impl.Message;
import info5.sar.MixedMessageQueue.Impl.Task;

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
