package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Impl.Event.SendEvent;

public class MessageQueue extends info5.sar.EventBasedMessageQueue.Abstract.MessageQueue{

	private boolean _closed;
	private ChannelImpl _channel;
	private MessageListener _listener;
	
	public MessageQueue(ChannelImpl channel) {
		super();
		_channel = channel;
		_closed = false;
	}
	
	@Override
	public void setListener(MessageListener listener) {
		_listener = listener;
	}

	public MessageListener getListener() {
		return _listener;
	}
	
	@Override
	public void send(Message message) {
		Task task = new Task();
		SendEvent sendEvent = new SendEvent(task, this, message, _listener);
		task.post(sendEvent);
	}

	@Override
	public void close() {
		_channel.disconnect();
	}

	@Override
	public boolean closed() {
		return _closed;
	}
	
	public ChannelImpl channel() {
		return _channel;
	}

}
