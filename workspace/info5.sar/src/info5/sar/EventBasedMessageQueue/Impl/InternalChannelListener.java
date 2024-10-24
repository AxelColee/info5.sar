package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Channel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;

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

	@Override
	public void disconnected() {
		_listener.closed();
	}

	@Override
	public void read(byte[] bytes) {
		if(_receivingState.equals(ReceivingState.Message)) {
			_listener.received(bytes);
			_receivingState = ReceivingState.Length;
		    byte[] length = new byte[4];

			new Task().post(() -> _channel.read(bytes));
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

	@Override
	public void wrote(byte[] bytes) {
		if(_sendingState.equals(SendingState.Message)) {
			_listener.sent(new Message(bytes));
			_sendingState = SendingState.Length;
		}else {
			_sendingState = SendingState.Message;
		}
		
	}
	
	public void setMessageListener(MessageListener ml) {
		_listener = ml;
	}

}
