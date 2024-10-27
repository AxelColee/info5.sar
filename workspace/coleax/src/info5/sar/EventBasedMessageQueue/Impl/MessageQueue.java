package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;

public class MessageQueue implements IMessageQueue{
	
	private IChannel _channel;
	
	public MessageQueue(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void setListener(MessageListener listener) {
		((InternalChannelListener)_channel.getListener()).setMessageListener(listener);
	}

	@Override
	public boolean send(Message message) {
		
		Task task = new Task();
		
		
		int length = message.getLength();
		
		byte[] size = new byte[4];
	    size[0] = (byte) ((length  & 0xFF000000) >> 24);
	    size[1] = (byte) ((length  & 0x00FF0000) >> 16);
	    size[2] = (byte) ((length  & 0x0000FF00) >> 8);
	    size[3] = (byte) (length  & 0x000000FF);
	    
	    task.post(() -> _channel.write(size));
	    
	    byte[] msg = new byte[length];
	    System.arraycopy(message.getBytes(), message.getOffset(), msg, 0 , length);
	    
	    task.post(() -> _channel.write(msg));
	    
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
