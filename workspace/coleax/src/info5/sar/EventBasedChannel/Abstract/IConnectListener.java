package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IConnectListener} interface provides methods to handle connection events.
 * This interface is implemented to define actions to be taken when a connection is
 * either successfully established or refused.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #connected(IChannel)}: Called when a connection is established.</li>
 * <li>{@link #refused()}: Called when a connection is refused.</li>
 * </ul>
 * 
 */
public interface IConnectListener {

	/**
	 * Called when a connection is established.
	 * @param channel The channel that was connected.
	 */
	public void connected(IChannel channel);

	/**
	 * Called when a connection is refused.
	 */
	public void refused();
}
