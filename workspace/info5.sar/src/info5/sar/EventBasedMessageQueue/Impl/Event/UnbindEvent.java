package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class UnbindEvent extends Event {

	private QueueBroker _queueBroker;
	private int _port;
	
	
	public UnbindEvent(Task task, QueueBroker queueBroker, int port) {
		super(task);
		_queueBroker = queueBroker;
		_port = port;
	}

	@Override
	protected void toDo() {
		_queueBroker.removeBind(_port);
	}

}
