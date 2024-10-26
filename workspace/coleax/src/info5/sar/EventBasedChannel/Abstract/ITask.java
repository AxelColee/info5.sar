package info5.sar.EventBasedChannel.Abstract;

public interface ITask {

	public abstract void post(Runnable r);
    public static ITask task() {
    	throw new IllegalStateException("ITask task() method called");
    }
    public abstract void kill();
    public abstract boolean killed();

}