package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;

/**
 * {@code MessageQueue} is an implementation of the {@link IMessageQueue} interface, providing a mechanism
 * to send and receive messages over an {@link IChannel} with structured handling of message events.
 * 
 * <p>This class wraps an {@link IChannel} and manages the communication through it by setting a
 * {@link MessageListener} to handle incoming messages and sending outgoing messages in a structured way.
 * It also provides methods to close the channel and check its connection status.</p>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #MessageQueue(IChannel)}: Initializes the message queue with a specified channel.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #setListener(MessageListener)}: Sets the {@code MessageListener} to handle message events for this queue.</li>
 *   <li>{@link #send(byte[])}: Sends a message through the channel, prefixing it with its length.</li>
 *   <li>{@link #close()}: Closes the channel associated with this message queue.</li>
 *   <li>{@link #closed()}: Checks if the channel is currently disconnected.</li>
 * </ul>
 * 
 * 
 * <p><strong>Design Note:</strong> This class is intended for use in event-driven message queues where
 * messages need to be handled in a structured format with length-prefixed messages.</p>
 * 
 * @see IMessageQueue
 * @see IChannel
 * @see MessageListener
 */
public class MessageQueue implements IMessageQueue{
	
	private IChannel _channel;
	private MessageListener _listener;
	
	public MessageQueue(IChannel channel) {
		_channel = channel;
	}

	/**
	 * Sets the {@code MessageListener} to handle message events for this queue.
	 * @param listener The listener to set.
	 */
	@Override
	public void setListener(MessageListener listener) {
		_listener = listener;
		((InternalChannelListener)_channel.getListener()).setMessageListener(_listener);
	}


	/**
	 * Sends a message through the channel, prefixing it with its length.
	 * @param message The message to send.
	 * @return {@code true} if the message was sent successfully, {@code false} otherwise.
	 */
	@Override
	public boolean send(byte[] message) {
		int length = message.length;
		
		byte[] size = new byte[4];
	    size[0] = (byte) ((length  & 0xFF000000) >> 24);
	    size[1] = (byte) ((length  & 0x00FF0000) >> 16);
	    size[2] = (byte) ((length  & 0x0000FF00) >> 8);
	    size[3] = (byte) (length  & 0x000000FF);
	    _channel.write(size);
	    _channel.write(message);
	    
	    return true;
	    
	}

	/**
	 * Closes the channel associated with this message queue.
	 */
	@Override
	public void close() {
		_channel.disconnect();
	}

	/**
	 * Checks if the channel is currently disconnected.
	 * @return {@code true} if the channel is disconnected, {@code false} otherwise.
	 */
	@Override
	public boolean closed() {
		return _channel.disconnected();
	}

}
