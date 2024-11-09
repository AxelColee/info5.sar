package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;

public class MessageQueue implements IMessageQueue{
	
	private MessageListener _listener;
	private IChannel _channel;
	
	public MessageQueue(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void setListener(MessageListener listener) {
		_listener = listener;
		((InternalChannelListener)_channel.getListener()).setMessageListener(_listener);
	}

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

	@Override
	public void close() {
		_channel.disconnect();
	}

	@Override
	public boolean closed() {
		return _channel.disconnected();
	}

}
