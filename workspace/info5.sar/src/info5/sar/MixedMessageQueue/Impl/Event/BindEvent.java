package info5.sar.MixedMessageQueue.Impl.Event;

import info5.sar.MixedMessageQueue.Abstract.QueueBroker.AcceptListener;
import info5.sar.MixedMessageQueue.Impl.QueueBroker;
import info5.sar.MixedMessageQueue.Impl.Task;

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
