package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Impl.Task;

public abstract class Event implements Runnable{
	
	private Task _task;
	
	public Event(Task task) {
		_task = task;
	}
	
	public Task task() {
		return _task;
	}
	
	@Override
	public void run() {
		Task.setCurrentTask(_task);
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	            toDo();
	        }
	    }).start();
	}
	
	protected abstract void toDo();
}
