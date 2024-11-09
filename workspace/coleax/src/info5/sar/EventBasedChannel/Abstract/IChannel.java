package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IChannel} interface defines the methods for a communication channel.
 * Implementations of this interface are expected to handle the transmission
 * and reception of data, manage listeners, and handle connection states.
 */
public interface IChannel {

	/**
	 * Connects to the specified host on the specified port.
	 * 
	 * @param host The host to connect to.
	 * @param port The port to connect to.
	 * @return {@code true} if the connection was successful, else {@code false}.
	 */
	public abstract void setListener(IChannelListener listener);

	/**
	 * Returns the listener for the channel.
	 * @return The listener for the channel.
	 */
	public abstract IChannelListener getListener();

	/**
	 * Connects to the specified host on the specified port.
	 * 
	 * @param host The host to connect to.
	 * @param port The port to connect to.
	 * @return {@code true} if the connection was successful, else {@code false}.
	 */
	public abstract boolean read(byte[] bytes);

	/**
	 * Writes the specified data to the channel.
	 * 
	 * @param bytes The data to write to the channel.
	 * @return {@code true} if the data was successfully written, else {@code false}.
	 */
	public abstract boolean write(byte[] bytes);

	/**
	 * Disconnects the channel.
	 */
	public abstract void disconnect();

	/**
	 * Returns {@code true} if the channel is disconnected, else {@code false}.
	 * @return {@code true} if the channel is disconnected, else {@code false}.
	 */
	public abstract boolean disconnected();
	
}
