package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.Channel.Exception.DisconnectedException;
import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.Message;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class SendEvent extends Event{
	

	private MessageQueue _mq;
	private Message _msg;
	private MessageListener _listener;
	
	public SendEvent(Task task, MessageQueue channel, Message msg, MessageListener listener) {
		super(task);
		_mq = channel;
		_msg = msg;
		_listener = listener;
	}

	@Override
	protected void toDo() {
		
		int nbSentBytes = 0;
		int length = _msg.getLength();
		ChannelImpl channel = _mq.channel();
		
		byte[] size = new byte[4];
	    size[0] = (byte) ((length  & 0xFF000000) >> 24);
	    size[1] = (byte) ((length  & 0x00FF0000) >> 16);
	    size[2] = (byte) ((length  & 0x0000FF00) >> 8);
	    size[3] = (byte) (length  & 0x000000FF);
	    
	    
	    
	    while (nbSentBytes < 4) {
	    	try {
				nbSentBytes = channel.write(size, 0 + nbSentBytes, 4 - nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		} 
	    
		
		nbSentBytes = 0;

		while(nbSentBytes < length) {
			try {
				nbSentBytes += channel.write(_msg.getBytes(), _msg.getOffset() + nbSentBytes, _msg.getLength()-nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		
		_listener.sent(_msg);
	}

}
