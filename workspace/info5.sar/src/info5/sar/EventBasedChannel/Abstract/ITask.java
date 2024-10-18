package info5.sar.EventBasedChannel.Abstract;

import info5.sar.MixedMessageQueue.Abstract.Task;

public interface ITask {

	public abstract void post(Runnable r);
    public static Task task() {
        return null;
    }
    public abstract void kill();
    public abstract boolean killed();

}
