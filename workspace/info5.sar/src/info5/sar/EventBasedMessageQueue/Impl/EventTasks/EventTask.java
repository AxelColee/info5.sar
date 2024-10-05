package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Impl.EventPump;

public abstract class EventTask implements Runnable {
	
	private boolean _killed;
	protected EventPump _eventPump;
	
	public EventTask() {
		_eventPump = EventPump.getInstance();
		_killed = false;
	}
	

	public void post(Runnable r) {
		_eventPump.post(r);
	}

	public void kill() {
		this._killed = true;
	}

	public boolean killed() {
		return _killed;
	}

	public static EventTask getTask() {
		return (EventTask) EventPump.getCurrentRunnable();
	}

}
