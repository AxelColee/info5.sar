package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IAcceptListener} interface defines a listener for accepting channels.
 * Implementations of this interface define the behavior
 * when a channel is accepted.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #accepted(IChannel)}: Called when a channel is accepted.</li>
 * </ul>
 * 
 */
public interface IAcceptListener {
	
	/**
	 * Called when a channel is accepted.
	 * @param channel The channel that was accepted.
	 */
	public void accepted(IChannel channel);

}
