package info5.sar.MixedMessageQueue.Impl.Event;

import info5.sar.MixedMessageQueue.Impl.QueueBroker;
import info5.sar.MixedMessageQueue.Impl.Task;

public class UnbindEvent extends Event {

	private QueueBroker _queueBroker;
	private int _port;
	
	
	public UnbindEvent(Task task, QueueBroker queueBroker, int port) {
		super(task);
		_queueBroker = queueBroker;
		_port = port;
	}

	
	@Override
	protected void _perform() {
		_queueBroker._unbind(_port);
	}

}
