package info5.sar.Channel.Abstract;

public abstract class Task extends Thread{
	
    protected Task(Broker b, Runnable r){};
	
	public abstract Broker getBroker();

}
