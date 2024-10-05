package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;

public class UnbindEventTask extends EventTask{
	
	private QueueBroker _broker;
	private int _port;
	
	public UnbindEventTask(QueueBroker broker, int port) {
		super();
		_broker = broker;
		_port = port;
		super.post(this);
	}

	@Override
	public void run() {
		if(_broker._unbind(_port)) {
			this.kill();
		}
	}

}
