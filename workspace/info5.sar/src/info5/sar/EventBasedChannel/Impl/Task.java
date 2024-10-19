package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.EventBasedChannel.Abstract.ITask;

public class Task implements ITask {
	
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
		Event event = new Event(task(), this, r);
		_events.add(event);
		_eventPump.post(event);
	}
	
	@Override
	public void kill() {
		this._killed = true;
	}

	@Override
	public boolean killed() {
		return _killed;
	}

	public static Task task() {
		Event e = EventPump.getCurrentEvent();
		if(e !=  null) {
			e.getTask();
		}
		return null;
	}

}
