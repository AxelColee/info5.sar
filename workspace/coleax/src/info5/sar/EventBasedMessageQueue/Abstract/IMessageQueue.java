package info5.sar.EventBasedMessageQueue.Abstract;


/**
 * The {@code IMessageQueue} interface defines the contract for a message queue system.
 * It provides methods to set a listener for incoming messages, send messages,
 * close the queue, and check if the queue is closed.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #setListener(MessageListener)}: Sets a listener for incoming messages.</li>
 * <li>{@link #send(byte[])}: Sends a message to the queue.</li>
 * <li>{@link #close()}: Closes the queue.</li>
 * <li>{@link #closed()}: Checks if the queue is closed. Returns {@code true} if so, else returns {@code false}.</li>
 * </ul>
 * 
 * @see MessageListener
 */
public interface IMessageQueue {
	    
	    public abstract void setListener(MessageListener listener);
	    public abstract boolean send(byte[] message);
	    public abstract void close();
	    public abstract boolean closed();

}
