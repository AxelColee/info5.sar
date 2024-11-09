package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IChannel} interface defines the methods for a communication channel.
 * Implementations of this interface are expected to handle the transmission
 * and reception of data, manage listeners, and handle connection states.
 */
public interface IChannel {

	public abstract void setListener(IChannelListener listener);
	public abstract IChannelListener getListener();
	public abstract boolean read(byte[] bytes);
	public abstract boolean write(byte[] bytes);
	public abstract void disconnect();
	public abstract boolean disconnected();
	
}
