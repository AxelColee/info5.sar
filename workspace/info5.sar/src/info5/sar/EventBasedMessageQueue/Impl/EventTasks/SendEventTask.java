package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;

public class SendEventTask extends EventTask{
	
	private MessageQueue _queue;
	private Message _msg;
	
	public SendEventTask(MessageQueue queue, Message msg) {
		super();
		_queue = queue;
		_msg = msg;
		super.post(this);

	}

	@Override
	public void run() {
		if(_queue._send(_msg)) {
			this.kill();
		}
	}

}
