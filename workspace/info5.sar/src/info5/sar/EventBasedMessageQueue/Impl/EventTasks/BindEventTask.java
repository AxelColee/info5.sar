package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;

public class BindEventTask extends EventTask{
	
	private QueueBroker _broker;
	private int _port;
	private AcceptListener _listener;
	
	public BindEventTask(QueueBroker broker, int port, AcceptListener listener) {
		super();
		_broker = broker;
		_port = port;
		_listener = listener;
		super.post(this);
	}

	@Override
	public void run() {
		if(_broker._bind(_port, _listener)) {
			this.kill();
		}
	}

}
