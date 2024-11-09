package info5.sar.EventBasedMessageQueue.Test.EchoServer.Server;

import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;

public class EchoServerMessageListener extends MessageListener{
	
	private int _nbClient;
	private static int _counter = 0;

	public EchoServerMessageListener(IMessageQueue queue, int nbClient) {
		super(queue);
		_nbClient = nbClient;
	}

	@Override
	public void received(byte[] bytes) {
		_queue.send(bytes);
	}

	@Override
	public void closed() {
		
		_counter++;
		
		assert(_queue != null) : "Server queue not initialized";
		assert(_queue.closed() == true) : "Server queue not disconnected";
		
		if(_counter >= _nbClient) {
			_counter = 0;
			System.out.println("Server passed");
			
		}
	}

	@Override
	public void sent(byte[] message) {
			_queue.close();
	}


}
