package info5.sar.EventBasedMessageQueue.Impl;

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
    	Runnable r = _runnables.poll();
    	_currentRunnable = r;
    	return r;
    }
    
    public static Runnable getCurrentRunnable() {
    	return _currentRunnable;
    }
    
    synchronized private boolean runnbaleIsEmpty() {
    	return _runnables.isEmpty();
    }
    
    public void stopPump() {
    	this._running = false;
    }
    
    
    @Override
    public void run() {

        while (_running) {
        	
        	synchronized (this) {
        		while (this.runnbaleIsEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
			}
            		
        	this.getNext();
        
        	try {
        		_currentRunnable.run();
            } catch (Exception e) {
            	
            }

            	 
        }
        
    }
}
