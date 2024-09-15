package info5.sar.task1;

public abstract class Task extends Thread{
	
	protected Task(Broker b, Runnable r){};
	
	/**
	 * 
	 * @return IBroker
	 */
	public abstract Broker getBroker();
}
