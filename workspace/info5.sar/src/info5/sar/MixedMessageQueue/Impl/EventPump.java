package info5.sar.MixedMessageQueue.Impl;

import java.util.LinkedList;
import java.util.Queue;

public class EventPump extends Thread{
	
	private static final EventPump INSTANCE = new EventPump();

	private boolean _running;
	private Queue<Runnable> _runnables;
	private static Runnable _currentRunnable;
	
	private EventPump() {
		_runnables = new LinkedList<Runnable>();
		_running = true;
	}
	
	public static EventPump getInstance() {
        return INSTANCE;
    }

	synchronized public void post(Runnable runnable) {
		_runnables.add(runnable);
		this.notify();
	}

	synchronized public void unpost(Runnable runnable) {
		_runnables.remove(runnable);
	}

    synchronized private Runnable getNext() {
    	_currentRunnable= _runnables.poll();
    	return _currentRunnable;
    }
    
    public static Runnable getCurrentRunnable() {
    	return _currentRunnable;
    }
    
    synchronized private boolean runnbaleIsEmpty() {
    	return _runnables.isEmpty();
    }
    
    synchronized public void stopPump() {
    	this._running = false;
    	this.notifyAll();
    }
    
    
    @Override
    public void run() {

        while (_running) {
    		while (this.runnbaleIsEmpty() && _running) {
    			synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
			}
        	
        	if(!_running) {
        		return;
        	}
            		
        	this.getNext();
        
        	try {
        		_currentRunnable.run();
            } catch (Exception e) {
            	
            }

            	 
        }
        
    }
}
