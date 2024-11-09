package info5.sar.EventBasedMessageQueue.Abstract;


/**
 * The IQueueBroker interface provides methods for managing the binding and 
 * connection of ports and listeners in an event-based message queue system.
 * Implementations of this interface are expected to handle the binding and
 * connection of ports, as well as the unbinding of ports.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #name()}: Returns the name of the queue broker.</li>
 * <li>{@link #unbind(int)}: Unbinds a port from the queue broker.</li>
 * <li>{@link #bind(int, IAcceptListener)}: Binds a port to the queue broker.</li>
 * <li>{@link #connect(String, int, IConnectListener)}: Connects to another queue broker on a specified port.</li>
 * </ul>
 * 
 * @see IAcceptListener
 * @see IConnectListener
 * 
 */
public interface IQueueBroker {
	
	public abstract String name();
    
    public abstract boolean unbind(int port);
    
    public abstract boolean bind(int port, IAcceptListener listener);
    
    public abstract boolean connect(String name, int port, IConnectListener listener);

}
