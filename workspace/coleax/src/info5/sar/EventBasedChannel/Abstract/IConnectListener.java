package info5.sar.EventBasedChannel.Abstract;

/**
 * The IConnectListener interface provides methods to handle connection events.
 * This interface is implemented to define actions to be taken when a connection is
 * either successfully established or refused.
 */
public interface IConnectListener {
	public void connected(IChannel channel);
	public void refused();
}
