package info5.sar.EventBasedMessageQueue.Impl;

public class Task extends info5.sar.EventBasedMessageQueue.Abstract.Task{
	
	private static Task _currentTask;
	private boolean _killed;
	private EventPump _eventPump;
	private Runnable _runnable;
	
	public Task() {
		_eventPump = EventPump.getInstance();
		_killed = false;
	}

	@Override
	public void post(Runnable r) {
		_runnable = r;
		_eventPump.post(r);
	}
	
	public void kill() {
		this._killed = true;
	}

	public boolean killed() {
		return _killed;
	}

	public static Task getTask() {
		return _currentTask;
	}
	
	public static void setCurrentTask(Task task) {
		_currentTask = task;
	}
	

}
