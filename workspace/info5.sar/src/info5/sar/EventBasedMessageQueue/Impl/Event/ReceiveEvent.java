package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.Channel.Exception.DisconnectedException;
import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Abstract.MessageQueue.MessageListener;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class ReceiveEvent extends Event{
	

	private MessageQueue _mq;
	private MessageListener _listener;
	
	public ReceiveEvent(Task task, MessageQueue channel, MessageListener listener) {
		super(task);
		_mq = channel;
		_listener = listener;
	}


	@Override
	protected void toDo() {
		
		do{
			int nbReceivedBytes = 0;
			ChannelImpl channel = _mq.channel();
			
			byte[] size = new byte[4];
		    
			while (nbReceivedBytes < 4) {
		    	try {
		    		nbReceivedBytes = channel.read(size, 0 + nbReceivedBytes, 4 - nbReceivedBytes);
				} catch (DisconnectedException e) {
					//e.printStackTrace();
					return;
				}
			} 
		    
		    int length = ((size[0] & 0xFF) << 24) |
	                ((size[1] & 0xFF) << 16) |
	                ((size[2] & 0xFF) << 8) |
	                (size[3] & 0xFF);
		    
		    byte[] message = new byte[length];
		    
		    nbReceivedBytes = 0;
			while(nbReceivedBytes < length) {
				try {
					nbReceivedBytes += channel.read(message, 0 + nbReceivedBytes, length-nbReceivedBytes);
				} catch (DisconnectedException e) {
					//e.printStackTrace();
					return;
				}
			}
			
			
			_listener.received(message);
		}while(!_mq.closed()) ;
		
	}
	

		

}