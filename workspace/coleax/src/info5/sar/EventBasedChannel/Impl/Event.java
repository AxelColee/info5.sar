package info5.sar.EventBasedChannel.Impl;


public class Event implements Runnable{
	private final Task _myTask;
	private final Runnable _runnable;
	
	
	public Event(Task mytask, Runnable r) {
		_myTask = mytask;
		_runnable = r;
	}

	
	@Override
	public void run() {
		this._runnable.run();
	}
	
	public Task getTask() {
		return _myTask;
	}
}
