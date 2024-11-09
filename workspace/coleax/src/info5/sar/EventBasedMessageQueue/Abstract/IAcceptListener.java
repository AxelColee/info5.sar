package info5.sar.EventBasedMessageQueue.Abstract;

/**
 * This interface defines a listener for accepting messages in a message queue.
 * Implementations of this interface provide the logic for handling
 * the acceptance of messages.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #accepted(IMessageQueue)}: Called when a message is accepted in the queue.</li>
 * </ul>
 * 
 * @see IMessageQueue
 */
public abstract interface IAcceptListener {
	
    public abstract void accepted(IMessageQueue queue);
   
}
