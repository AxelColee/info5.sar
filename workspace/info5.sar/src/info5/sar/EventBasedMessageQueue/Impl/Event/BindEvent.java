package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class BindEvent extends Event {
	
	private QueueBroker _broker;
	private int _port;
	private AcceptListener _listener;

	public BindEvent(Task task, QueueBroker broker, int port, AcceptListener listener) {
		super(task);
		_broker = broker;
		_port = port;
		_listener = listener;
	}

	@Override
	protected void toDo() {
		do {
			
			_broker.addBind(_port, _listener);
			
			ChannelImpl channelAccept = (ChannelImpl) _broker.getBroker().accept(_port);
			
			MessageQueue mq = new MessageQueue(channelAccept);
			
			_listener.accepted(mq);
			
			Task task = new Task();
			ReceiveEvent receievEvent = new ReceiveEvent(task, mq, mq.getListener());
			task.post(receievEvent);
			
		}while(_broker.getBind(_port));
		
	}

}
