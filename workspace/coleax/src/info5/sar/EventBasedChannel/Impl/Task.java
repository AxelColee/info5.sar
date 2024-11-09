package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.EventBasedChannel.Abstract.ITask;

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

	@Override
	public void post(Runnable r) {
		if(!_killed && _remaining_tries != 0) {
			Event event = new Event(this, r);
			_events.add(event);
			_eventPump.post(event);
		}
	}
	
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
	
	@Override
	public int getRemainingTries() {
		return _remaining_tries;
	}
	
	public static Task task() {
		Event e = EventPump.getCurrentEvent();
		if(e !=  null) {
			return e.getTask();
		}
		return null;
	}

}
