package info5.sar.EventBasedMessageQueue.Test.Server;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker;

public class EchoServerMessageListener implements MessageListener{
	
	private MessageQueue _queue;
	private QueueBroker _broker;
	private int cpt = 0;
	
	public EchoServerMessageListener(MessageQueue queue, QueueBroker broker) {
		_queue = queue;
		_broker = broker;
	}

	@Override
	public void received(byte[] bytes) {
		_queue.send(new Message(bytes, 0, bytes.length));
		if(++cpt >= 3) {
			_broker.unbind(80);
		}
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

	}

}
