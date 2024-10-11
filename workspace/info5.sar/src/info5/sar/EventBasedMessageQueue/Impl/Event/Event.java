package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Impl.Task;

public abstract class Event implements Runnable{
	
	private final Task _task;
	
	public Event(Task task) {
		_task = task;
	}
	
	public Task task() {
		return _task;
	}
	
	@Override
	public void run() {
		Task.setCurrentTask(_task);
		_perform();
	}
	
	protected abstract void _perform();
}
