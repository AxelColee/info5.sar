package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Task;

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
