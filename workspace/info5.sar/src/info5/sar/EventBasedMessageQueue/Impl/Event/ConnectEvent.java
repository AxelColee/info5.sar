package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.ConnectListener;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class ConnectEvent extends Event{
	
	private QueueBroker _broker;
	private String _name; 
	private int _port;
	private ConnectListener _listener;
	
	public ConnectEvent(Task task, QueueBroker broker, String name, int port, ConnectListener listener) {
		super(task);
		_broker = broker;
		_name = name;
		_port = port;
		_listener = listener;
	}

	@Override
	protected void _perform() {
		_broker._connect(_name, _port, _listener);

	}

}
