package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;

/**
 * {@code InternalConnectListener} is an implementation of the {@link IConnectListener} interface.
 * It acts as a decorator around another {@link IConnectListener} instance, adding extra functionality
 * when a connection is either established or refused.
 * 
 * <p>This class is responsible for setting up a {@link MessageQueue} and an {@link InternalChannelListener}
 * on the {@link IChannel} when a connection is established. It also initiates the first read operation to
 * start message processing. Events are then delegated to the wrapped {@link IConnectListener} instance.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _listener} - The {@link IConnectListener} instance to which events are delegated after additional setup.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #InternalConnectListener(IConnectListener)}: Initializes the listener with the specified {@code IConnectListener} to wrap.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #connected(IChannel)}: Called when a connection is successfully established. Posts a task to read the message length and delegates this event to the wrapped listener.</li>
 *   <li>{@link #refused()}: Called when a connection attempt is refused, delegating this event to the wrapped listener.</li>
 * </ul>
 * 
 * @see IConnectListener
 * @see IChannel
 * @see MessageQueue
 * @see InternalChannelListener
 */
public class InternalConnectListener implements info5.sar.EventBasedChannel.Abstract.IConnectListener{
	
	private IConnectListener _listener;
	
	public InternalConnectListener(IConnectListener listener) {
		_listener = listener;
	}

	/**
	 * Called when a connection is established.
	 * @param queue
	 */
	@Override
	public void connected(IChannel channel) {
		
		MessageQueue queue = new MessageQueue(channel);
		
		channel.setListener(new InternalChannelListener(channel));
		
		byte[] length = new byte[4];
		new Task().post(() -> channel.read(length));
		
		_listener.connected(queue);
	}


	/**
	 * Called when a connection is refused.
	 *@param queue
	 */
	@Override
	public void refused() {
		_listener.refused();
		
	}
}