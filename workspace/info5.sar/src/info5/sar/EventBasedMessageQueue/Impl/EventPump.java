package info5.sar.EventBasedMessageQueue.Impl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import info5.sar.EventBasedMessageQueue.Impl.EventTasks.EventTask;

public class EventPump extends Thread{
	
	private static final EventPump INSTANCE = new EventPump();

	private boolean _running;
	private Queue<Runnable> _runnables;
	private static Runnable _currentRunnable;
	private final long MAX_EXECUTION_TIME_MS = 500;
    private ExecutorService executorService;
	
	private EventPump() {
		_runnables = new LinkedList<Runnable>();
		executorService = Executors.newSingleThreadExecutor();
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
    	this.stop();
    	System.out.println("stopppp");
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
        
        	Future<?> future = executorService.submit(_currentRunnable);
        	
        	try {
        		future.get(MAX_EXECUTION_TIME_MS, TimeUnit.MILLISECONDS);          
            } catch (Exception e) {
            	// Nothing to do here
            	future.cancel(true);
            	executorService = Executors.newSingleThreadExecutor();
            }
        	
        	
        	if (!EventTask.getTask().killed()) {
                 this.post(_currentRunnable);
        	}
            	 
        }
        
    }
}


	

