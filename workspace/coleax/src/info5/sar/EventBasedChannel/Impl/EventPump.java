package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@code EventPump} class is a singleton event dispatcher that manages a queue of events,
 * providing a mechanism to sequentially process events in a single-threaded environment.
 * It allows for posting, removing, and executing events one at a time, ensuring controlled
 * event flow in event-driven architectures.
 * 
 * <p>This class is designed to handle events in a sequential manner, ensuring
 * that each event is processed one at a time.</p>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code _events} - A queue of events to be processed.</li>
 *   <li>{@code _currentEvent} - The current event being processed.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *     <li>{@link #getInstance()} - Returns the singleton instance of the EventPump.</li>
 *     <li>{@link #post(Event)} - Adds an event to the queue.</li>
 *     <li>{@link #unpost(Event)} - Removes an event from the queue.</li>
 *     <li>{@link #getCurrentEvent()} - Returns the current event being processed.</li>
 *     <li>{@link #removeAllEventOfaTask(Task)} - Removes all events associated with a specific task.</li>
 *     <li>{@link #run()} - Processes all events in the queue.</li>
 * </ul>
 * 
 * <p>Note: This class is not thread-safe and should be used in a single-threaded context.</p>
 * 
 * @see Event
 * @see Task
 */
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
