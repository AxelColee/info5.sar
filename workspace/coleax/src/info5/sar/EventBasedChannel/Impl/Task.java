package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.EventBasedChannel.Abstract.ITask;

/**
 * The {@code Task} class implements the ITask interface and represents a task that can post events to an EventPump.
 * It maintains a list of events and tracks the number of remaining tries for the task. This is useful because 
 * it allows the task to be retried a certain number of times before being killed.
 * 
 * This class provides methods for posting events, killing the task, and checking the task status.
 * The task can be killed, which prevents further events from being posted and removes all associated events from the EventPump.
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code MAX_TRIES} - The maximum number of tries for a task.</li>
 *   <li>{@code _killed} - A flag indicating whether the task has been killed.</li>
 *   <li>{@code _eventPump} - The EventPump instance used to post events.</li>
 *   <li>{@code _events} - A list of events to be processed.</li>
 *   <li>{@code _remaining_tries} - The number of remaining tries for the task.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *     <li>{@link #post(Runnable)} - Posts a runnable event to the EventPump.</li>
 *     <li>{@link #kill()} - Kills the task and removes all associated events from the EventPump.</li>
 *     <li>{@link #killed()} - Checks if the task has been killed.</li>
 *     <li>{@link #getRemainingTries()} - Returns the number of remaining tries for the task.</li>
 *     <li>{@link #task()} - Returns the current task associated with the current event.</li>
 * </ul>
 */
public class Task implements ITask {
	
	public static int MAX_TRIES = 10;
	
	
	private boolean _killed;
	private EventPump _eventPump;
	private List<Event> _events;
	private int _remaining_tries;
	
	public Task(int remainingTries) {
		this._remaining_tries = remainingTries;
		_eventPump = EventPump.getInstance();
		_killed = false;
		_events = new LinkedList<Event>();
	}
	
	public Task() {
		this(MAX_TRIES);
	}

	/**
	 * Posts a runnable event to the EventPump.
	 * @param r The runnable event to post
	 */
	@Override
	public void post(Runnable r) {
		if(!_killed && _remaining_tries != 0) {
			Event event = new Event(this, r);
			_events.add(event);
			_eventPump.post(event);
		}
	}
	
	/**
	 * Kills the task and removes all associated events from the EventPump.
	 */
	@Override
	public void kill() {
		if(!_killed) {
			this._killed = true;
			_eventPump.removeAllEventOfaTask(this);
		}
	}

	@Override
	public boolean killed() {
		return _killed;
	}
	
	/**
	 * Returns the number of remaining tries for the task.
	 * @return The number of remaining tries for the task
	 */
	@Override
	public int getRemainingTries() {
		return _remaining_tries;
	}
	
	/**
	 * Returns the current task associated with the current event.
	 * @return The current task {@link Task} associated with the current event
	 */
	public static Task task() {
		Event e = EventPump.getCurrentEvent();
		if(e !=  null) {
			return e.getTask();
		}
		return null;
	}

}
