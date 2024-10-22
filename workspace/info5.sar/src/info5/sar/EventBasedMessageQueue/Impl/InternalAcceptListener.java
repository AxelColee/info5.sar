package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.AcceptListener;

public class InternalAcceptListener implements info5.sar.EventBasedChannel.Abstract.IAcceptListener{
	
	private AcceptListener _listener;
	
	public InternalAcceptListener(AcceptListener listener) {
		_listener = listener;
	}

	@Override
	public void accepted(IChannel channel) {
		
		MessageQueue queue = new MessageQueue(channel);
		
		channel.setListener(new InternalChannelListener(_listener.getMessageListener(), queue, channel));
		
		byte[] length = new byte[4];
		new Task().post(() -> channel.read(length));
		
		_listener.accepted(queue);
	}

}
