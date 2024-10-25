package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;

public class InternalConnectListener implements info5.sar.EventBasedChannel.Abstract.IConnectListener{
	
	private IConnectListener _listener;
	
	public InternalConnectListener(IConnectListener listener) {
		_listener = listener;
	}

	@Override
	public void connected(IChannel channel) {
		
		MessageQueue queue = new MessageQueue(channel);
		
		channel.setListener(new InternalChannelListener(channel));
		
		byte[] length = new byte[4];
		new Task().post(() -> channel.read(length));
		
		_listener.connected(queue);
	}

	@Override
	public void refused() {
		_listener.refused();
		
	}
}