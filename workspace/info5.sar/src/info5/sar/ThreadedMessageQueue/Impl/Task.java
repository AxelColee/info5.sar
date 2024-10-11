package info5.sar.ThreadedMessageQueue.Impl;

import info5.sar.ThreadedChannel.Abstract.Broker;
import info5.sar.ThreadedMessageQueue.Abstract.QueueBroker;

public class Task extends info5.sar.ThreadedMessageQueue.Abstract.Task{
	
	private Broker _broker;
	private QueueBroker _queueBroker;
	private Runnable _runnable;

	protected Task(Broker b, Runnable r) {
		super(b, r);
		_broker = b;
		_runnable = r;
	}
	
	protected Task(QueueBroker b, Runnable r) {
		super(b, r);
		_queueBroker = b;
		_runnable = r;
	}

	@Override
	public Broker getBroker() {
		if(_broker != null) {
			return _broker;
		}
		throw new IllegalStateException("This task only has a QueueBroker");
	}

	@Override
	public QueueBroker getQueueBroker() {
		if(_queueBroker != null) {
			return _queueBroker;
		}
		throw new IllegalStateException("This task only has a Broker");
	}
	
	@Override
	public void run() {
		this._runnable.run();
	}

}
