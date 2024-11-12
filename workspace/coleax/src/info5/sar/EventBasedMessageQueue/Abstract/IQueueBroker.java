package info5.sar.EventBasedMessageQueue.Abstract;


/**
 * The IQueueBroker interface provides methods for managing the binding and 
 * connection of ports and listeners in an event-based message queue system.
 * Implementations of this interface are expected to handle the binding and
 * connection of ports, as well as the unbinding of ports.
 * 
 * <p><strong>Methods:</strong></p>
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
	
    /**     
     * Returns the name of the queue broker.
     * @return The name of the queue broker.
     */
	public abstract String name();
    
    /**
     * Unbinds the specified port from the queue broker.
     * 
     * @param port The port to unbind.
     * @return {@code true} if the port was successfully unbound, else {@code false}.
     */
    public abstract boolean unbind(int port);
    

    /**
     * Binds the specified port to the queue broker.
     * 
     * @param port The port to bind.
     * @param listener The listener to bind to the port.
     * @return {@code true} if the port was successfully bound, else {@code false}.
     */
    public abstract boolean bind(int port, IAcceptListener listener);
    

    /**
     * Connects to another queue broker on the specified port.
     * 
     * @param name The name of the queue broker to connect to.
     * @param port The port to connect to.
     * @param listener The listener to handle the connection.
     * @return {@code true} if the connection was successful, else {@code false}.
     */
    public abstract boolean connect(String name, int port, IConnectListener listener);

}
