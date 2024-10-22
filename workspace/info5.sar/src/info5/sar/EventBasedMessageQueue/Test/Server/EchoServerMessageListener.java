package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.Message;

public class EchoServerMessageListener extends MessageListener{

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
		
		assert(_queue != null) : "Server queue not initialized";
		assert(_queue.closed() == true) : "Server queue not disconnected";
		
		System.out.println("Server passed");

	}

	@Override
	public void sent(Message message) {
		_queue.close();

	}


}
