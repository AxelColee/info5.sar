package info5.sar.EventBasedMessageQueue.Impl;


import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Broker;
import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;


/**
 * {@code QueueBroker} is an implementation of the {@link IQueueBroker} interface that wraps an internal
 * {@link Broker} instance to provide message queue-specific binding and connection management.
 * 
 * This class extends the functionality of the base {@link Broker} class to manage event-based
 * message queuing, making it easier to bind listeners and handle connections.
 * 
 * <p>It provides methods to bind, unbind, and connect to ports, delegating these operations to the
 * internal {@link Broker} instance while adapting listeners to handle message queuing events.</p>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #QueueBroker(String)}: Initializes the {@code QueueBroker} with a specific name, creating an internal {@link Broker} instance.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #name()}: Returns the name of the internal broker.</li>
 *   <li>{@link #unbind(int)}: Unbinds a specified port, stopping any listening activity on that port.</li>
 *   <li>{@link #bind(int, IAcceptListener)}: Binds a specified port to an {@link IAcceptListener} adapted for message queuing, enabling connection acceptance on that port.</li>
 *   <li>{@link #connect(String, int, IConnectListener)}: Connects to a remote broker by name and port, delegating to an {@link IConnectListener} adapted for queuing events.</li>
 * </ul>
 * 
 * @see IQueueBroker
 * @see IBroker
 * @see Broker
 * @see InternalAcceptListener
 * @see InternalConnectListener
 */
public class QueueBroker implements IQueueBroker{
	
	private IBroker _broker;
	
	public QueueBroker(String name) {
		_broker = (Broker) new Broker(name);
	}

	/**
	 * Returns the name of the broker.
	 * @return the name of the broker
	 * @see Broker#name()
	 */
	@Override
	public String name() {
		return _broker.name();
	}

	/**
	 * Unbinds the listener from the specified port.
	 * @param port the port number
	 * @return true if the listener was successfully unbound, false otherwise
	 * @see Broker#unbind(int)
	 */
	@Override
	public boolean unbind(int port) {
		return _broker.unbind(port);
	}

	/**
	 * Binds the listener to the specified port.
	 * @param port the port number
	 * @param listener the listener to bind
	 * @return true if the listener was successfully bound, false otherwise
	 * @see Broker#bind(int, IAcceptListener)
	 */
	@Override
	public boolean bind(int port, IAcceptListener listener) {
		return _broker.bind(port, new InternalAcceptListener(listener));
	}

	/**
	 * Connects to the broker with the specified name and port.
	 * @param name the name of the broker to connect to
	 * @param port the port number
	 * @param listener the listener to handle the connection
	 * @return true if the connection was successful, false otherwise
	 * @see Broker#connect(String, int, IConnectListener)
	 */
	@Override
	public boolean connect(String name, int port, IConnectListener listener) {
		return _broker.connect(name, port, new InternalConnectListener(listener));
	}

}
