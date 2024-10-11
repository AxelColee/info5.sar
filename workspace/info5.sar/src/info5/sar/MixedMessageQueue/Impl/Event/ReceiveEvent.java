package info5.sar.MixedMessageQueue.Impl.Event;

import info5.sar.MixedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.MixedMessageQueue.Impl.MessageQueue;
import info5.sar.MixedMessageQueue.Impl.Task;
import info5.sar.ThreadedChannel.Exception.DisconnectedException;
import info5.sar.ThreadedChannel.Impl.ChannelImpl;

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