package info5.sar.MixedMessageQueue.Impl.Event;

import info5.sar.MixedMessageQueue.Impl.Message;
import info5.sar.MixedMessageQueue.Impl.MessageQueue;
import info5.sar.MixedMessageQueue.Impl.Task;

public class SendEvent extends Event{
	

	private MessageQueue _mq;
	private Message _msg;
	
	public SendEvent(Task task, MessageQueue mq, Message msg) {
		super(task);
		_mq = mq;
		_msg = msg;
	}

	@Override
	protected void _perform() {
		
		_mq._send(_msg);
	}

}
