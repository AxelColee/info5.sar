package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.Queue;

public class EventPump {
	
	private static final EventPump INSTANCE = new EventPump();

	private Queue<Event> _events;
	private static Event _currentEvent;
	
	private EventPump() {
		_events = new LinkedList<Event>();
	}
	
	public static EventPump getInstance() {
        return INSTANCE;
    }

	 public void post(Event ev) {
		 _events.add(ev);
	}

	 public void unpost(Event ev) {
		 _events.remove(ev);
	}

    private Event getNext() {
    	_currentEvent = _events.poll();
    	return _currentEvent;
    }
    
    public static Event getCurrentEvent() {
    	return _currentEvent;
    }
    
    public void removeAllEventOfaTask(Task task) {
    	for(Event e : _events) {
    		if(e.getTask() == task) {
    			_events.remove(e);
    		}
    	}
    }
    
    
    public void run() {

        while (!_events.isEmpty()) {
            		
        	this.getNext();
        
        	try {
        		_currentEvent.run();
            } catch (Exception e) {
            	e.printStackTrace();
            	
            }
            	 
        }
        
    }

}
