package info5.sar.EventBasedChannel.Impl;


/**
 * The Event class implements the Runnable interface and represents an event
 * that can be executed. It contains a task and a runnable that will be run
 * when the event is executed.
 * 
 * @see Runnable
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #run()} - Executes the event's runnable.</li>
 * <li>{@link #getTask()} - Returns the task associated with the event.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 * <li>{@code _myTask} - The task associated with the event.</li>
 * <li>{@code _runnable} - The runnable that will be executed when the event is run.</li>
 * </ul>
 * 
 */
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
