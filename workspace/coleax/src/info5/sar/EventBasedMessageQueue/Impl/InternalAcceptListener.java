package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;

/**
* <p>This class serves as an intermediary between the accepted channel and the core message-handling
 * logic, providing additional setup before delegating to the wrapped {@link IAcceptListener}.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _listener} - The {@link IAcceptListener} instance that this class wraps, to which it delegates channel acceptance after setup.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #InternalAcceptListener(IAcceptListener)}: Constructs an {@code InternalAcceptListener} with a specified {@link IAcceptListener} to wrap.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #accepted(IChannel)}: Called when a channel is accepted. Initializes a {@link MessageQueue} for the channel, assigns an {@link InternalChannelListener} to handle channel events, and triggers reading of the initial message length.</li>
 * </ul>
 * 
 * @see IAcceptListener
 * @see IChannel
 * @see MessageQueue
 */
public class InternalAcceptListener implements info5.sar.EventBasedChannel.Abstract.IAcceptListener{
	
	private IAcceptListener _listener;
	
	public InternalAcceptListener(IAcceptListener listener) {
		_listener = listener;
	}

	@Override
	public void accepted(IChannel channel) {
		
		MessageQueue queue = new MessageQueue(channel);
		
		channel.setListener(new InternalChannelListener(channel));
		
		byte[] length = new byte[4];
		new Task().post(() -> channel.read(length));
		
		_listener.accepted(queue);
	}

}
