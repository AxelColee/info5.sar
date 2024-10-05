package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;

public class ReceiveEventTask extends EventTask{
	private MessageQueue _queue;
	
	public ReceiveEventTask(MessageQueue queue) {
		super();
		_queue = queue;
		super.post(this);
	}

	@Override
	public void run() {
		if(_queue._receive()) {
			this.kill();
		}
	}

}
