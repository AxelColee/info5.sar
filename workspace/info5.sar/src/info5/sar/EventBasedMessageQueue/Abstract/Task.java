package info5.sar.EventBasedMessageQueue.Abstract;


public abstract class Task extends Thread{
	
	private QueueBroker _broker;
	private Runnable _runnable;
	
	public Task(QueueBroker broker, Runnable runnable) {
		_broker = broker;
		_runnable = runnable;
	}
		 	
	protected QueueBroker getBroker() {
		return _broker;
	}
	
	protected static Task getTask() {
		return (Task) Thread.currentThread();
	}

	@Override
	public void run() {
		_runnable.run();
	}
	

}
