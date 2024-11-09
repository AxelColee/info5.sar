package info5.sar.EventBasedChannel.Abstract;

/**
 * The ITask interface provides a contract for task management, including methods
 * for posting tasks, checking task status, and managing task lifecycle.
 */
public interface ITask {

	public abstract void post(Runnable r);
    public static ITask task() {
    	throw new IllegalStateException("ITask task() method called");
    }
    public abstract void kill();
    public abstract boolean killed();
    public abstract int getRemainingTries();

}