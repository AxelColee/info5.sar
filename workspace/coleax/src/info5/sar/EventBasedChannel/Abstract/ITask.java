package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code ITask} interface provides a contract for task management, including methods
 * for posting tasks, checking task status, and managing task lifecycle.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #post(Runnable)}: Posts a task to be executed
 * <li>{@link #kill()}: Kills the task
 * <li>{@link #killed()}: Checks if the task has been killed
 * <li>{@link #getRemainingTries()}: Returns the number of remaining tries for the task
 * </ul>
 * 
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