package info5.sar.EventBasedMessageQueue.Abstract;

/**
 * The IConnectListener interface provides methods to handle connection events
 * for a message queue. Implementations of this interface can define custom
 * behavior for when a connection is established or refused.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #connected(IMessageQueue)}: Called when a connection is established.</li>
 * <li>{@link #refused()}: Called when a connection is refused.</li>
 * </ul>
 * 
 * @see IMessageQueue
 */
public abstract interface IConnectListener {
	
    public abstract void connected(IMessageQueue queue);
    public abstract void refused();

}
