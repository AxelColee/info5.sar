package info5.sar.EventBasedChannel.Abstract;

/**
 * This interface defines a listener for accepting channels.
 * Implementations of this interface should define the behavior
 * when a channel is accepted.
 */
public interface IAcceptListener {
	
	public void accepted(IChannel channel);

}
