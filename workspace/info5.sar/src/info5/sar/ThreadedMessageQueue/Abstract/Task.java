package info5.sar.ThreadedMessageQueue.Abstract;

import info5.sar.ThreadedChannel.Abstract.Broker;

public abstract class Task extends Thread{
	
	protected Task(Broker b, Runnable r){}
	
	protected Task(QueueBroker b, Runnable r){}
	 
	protected abstract Broker getBroker();
	
	protected abstract QueueBroker getQueueBroker();
	
	protected static Task getTask() {
		return (Task) Thread.currentThread();
	}

}
