package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.Channel.Exception.DisconnectedException;
import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class ReceiveEvent extends Event{
	

	private MessageQueue _mq;
	
	public ReceiveEvent(Task task, MessageQueue mq) {
		super(task);
		_mq = mq;
	}


	@Override
	protected void _perform() {
		
		_mq._receive();
		
	}
	

		

}