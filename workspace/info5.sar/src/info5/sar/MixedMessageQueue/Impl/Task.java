package info5.sar.MixedMessageQueue.Impl;

import java.util.LinkedList;
import java.util.List;

public class Task extends info5.sar.MixedMessageQueue.Abstract.Task{
	
	private static Task _currentTask;
	private boolean _killed;
	private EventPump _eventPump;
	private List<Event> _events;
	
	public Task() {
		_eventPump = EventPump.getInstance();
		_killed = false;
		_events = new LinkedList<Event>();
	}

	@Override
	public void post(Runnable r) {
		Event event = new Event(_currentTask, this, r);
		_events.add(event);
		_eventPump.post(event);
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
