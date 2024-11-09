package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IBroker} interface defines the contract for a broker that handles binding,
 * unbinding, and connecting operations. Implementations of this interface are
 * expected to manage network connections and communication channels.
 */
public interface IBroker {

	/**
	 * Returns the name of the broker.
	 * @return The name of the broker.
	 */
	String name();

	/**
	 * Binds the specified port to the broker.
	 * @param port The port to bind.
	 * @param listener The listener to bind to the port.
	 * @return {@code true} if the port was successfully bound, else {@code false}.
	 */
	boolean bind(int port, IAcceptListener listener);

	/**
	 * Unbinds the specified port from the broker.
	 * @param port The port to unbind.
	 * @return {@code true} if the port was successfully unbound, else {@code false}.
	 */
	boolean unbind(int port);

	/**
	 * Connects to another broker on the specified port.
	 * @param name The name of the broker to connect to.
	 * @param port The port to connect to.
	 * @param listener The listener to handle the connection.
	 * @return {@code true} if the connection was successful, else {@code false}.
	 */
	boolean connect(String name, int port, IConnectListener listener);

}
