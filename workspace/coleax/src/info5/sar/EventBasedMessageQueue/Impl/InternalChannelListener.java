package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;

/**
 * {@code InternalChannelListener} is an implementation of the {@link IChannelListener} interface
 * responsible for handling message transmission and reception over a given {@link IChannel}.
 * 
 * <p>This listener operates with two primary states for both sending and receiving data:
 * <ul>
 *   <li><strong>SendingState</strong>: Represents the stage of sending either the message length or the message content itself.</li>
 *   <li><strong>ReceivingState</strong>: Represents the stage of receiving either the message length or the actual message content.</li>
 * </ul>
 * 
 * <p>The listener utilizes a {@link MessageListener} to notify when messages are sent, received, or when the channel is closed,
 * enabling integration with other components interested in message events.</p>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #InternalChannelListener(IChannel)}: Initializes the listener with the specified {@link IChannel} and sets the initial sending and receiving states.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #disconnected()}: Called when the channel is disconnected. Notifies the {@link MessageListener} of the channel closure.</li>
 *   <li>{@link #read(byte[])}: Invoked upon reading bytes from the channel. Manages state transitions and handles either the length or message content based on the current {@code ReceivingState}.</li>
 *   <li>{@link #wrote(byte[])}: Invoked upon writing bytes to the channel. Manages state transitions and notifies the {@link MessageListener} when the message content is fully sent.</li>
 *   <li>{@link #setMessageListener(MessageListener)}: Assigns a {@link MessageListener} for handling message events.</li>
 * </ul>
 * 
 * @see IChannelListener
 * @see IChannel
 * @see MessageListener
 * 
 */
public class InternalChannelListener implements IChannelListener{
	
	private enum SendingState {
	    Length,
	    Message
	}
	
	private enum ReceivingState {
	    Length,
	    Message
	}
	
	private IChannel _channel;
	private MessageListener _listener;
	private SendingState _sendingState;
	private ReceivingState _receivingState;
	
	public InternalChannelListener(IChannel channel) {
		_sendingState = SendingState.Length;
		_receivingState = ReceivingState.Length;
		_channel = channel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnected() {
		_listener.closed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void read(byte[] bytes) {
		if(_receivingState.equals(ReceivingState.Message)) {
			_listener.received(bytes);
			_receivingState = ReceivingState.Length;

			new Task().post(() -> _channel.read(new byte[4]));
		}else {
			
			 int length = ((bytes[0] & 0xFF) << 24) |
		                ((bytes[1] & 0xFF) << 16) |
		                ((bytes[2] & 0xFF) << 8) |
		                (bytes[3] & 0xFF);
			    
			    byte[] message = new byte[length];
			    new Task().post(() -> _channel.read(message));
			_receivingState = ReceivingState.Message;

		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wrote(byte[] bytes) {
		if(_sendingState.equals(SendingState.Message)) {
			_listener.sent(bytes);
			_sendingState = SendingState.Length;
		}else {
			_sendingState = SendingState.Message;
		}
		
	}
	
	/**
	 * Assigns a {@link MessageListener} for handling message events.
	 * @param ml
	 */
	public void setMessageListener(MessageListener ml) {
		_listener = ml;
	}

}
