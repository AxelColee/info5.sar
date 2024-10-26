package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;

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
