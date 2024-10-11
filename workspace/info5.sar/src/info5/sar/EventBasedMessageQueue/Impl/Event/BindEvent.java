package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;
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
	protected void _perform() {
		_broker._bind(_port, _listener);
	}

}
