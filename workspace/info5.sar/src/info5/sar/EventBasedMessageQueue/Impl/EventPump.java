package info5.sar.EventBasedMessageQueue.Impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import info5.sar.EventBasedMessageQueue.Impl.EventTasks.EventTask;

public class EventPump extends Thread{
	
	private static final EventPump INSTANCE = new EventPump();

	private Queue<Runnable> _runnables;
	private static Runnable _currentRunnable;
	private final long MAX_EXECUTION_TIME_MS = 500;;
	
	private EventPump() {
		_runnables = new LinkedList<Runnable>();
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
    
    @Override
    public void run() {
        while (true) {
            synchronized (this) {

            	while (_runnables.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            	
            	this.getNext();
            	
            	Timer timer = new Timer(this);
            	timer.start();
            	
            	try {
                    _currentRunnable.run();
                    timer.interrupt();
                } catch (Exception e) {
                	// Nothing to do here
                }
            	
            	
            	if (!EventTask.getTask().killed()) {
                     this.post(_currentRunnable);
            	}
            	 
            }
        }
    }
}


	

