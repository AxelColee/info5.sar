package info5.sar.EventBasedMessageQueue.Impl;

import info5.sar.Channel.Exception.DisconnectedException;
import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.EventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.ReceiveEventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.SendEventTask;

public class MessageQueue extends info5.sar.EventBasedMessageQueue.Abstract.MessageQueue{
	
	private boolean _closed;
	private ChannelImpl _channel;
	private MessageListener _listener;
	private Message _msg;
	private Message _msgLength;
	private MessageQueue _rmq;
	
	public MessageQueue(ChannelImpl channel) {
		super();
		_channel = channel;
		_closed = false;
		_msgLength = new Message(4);
	}

	@Override
	public void setListener(MessageListener listener) {
		_listener = listener;
	}

	@Override
	public void send(Message message) {
		EventTask task = new SendEventTask(this, message);
		new ReceiveEventTask(_rmq);
	}
	
	public boolean _send(Message message) {
		int nbSentBytes = 0;
		
		int length = message.getLength();
		byte[] size = new byte[4];
	    size[0] = (byte) ((length  & 0xFF000000) >> 24);
	    size[1] = (byte) ((length  & 0x00FF0000) >> 16);
	    size[2] = (byte) ((length  & 0x0000FF00) >> 8);
	    size[3] = (byte) (length  & 0x000000FF);
	    
	    while (nbSentBytes < 4) {
	    	try {
				nbSentBytes = _channel.write(size, 0 + nbSentBytes, 4 - nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		} 
	    
		
		nbSentBytes = 0;
		while(nbSentBytes < length) {
			try {
				nbSentBytes += _channel.write(message.getBytes(), message.getOffset(), length-message.getOffset());
				message.setOffset(message.getOffset() + nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		
		_listener.sent(message);
		return true;
	}
	
	public boolean _receive() {
		int nbReceivedBytes = 0;
	    
		while (nbReceivedBytes < 4) {
	    	try {
	    		nbReceivedBytes += _channel.read(_msgLength.getBytes(), _msgLength.getOffset(), 4 - _msgLength.getOffset());
				_msgLength.setOffset(_msgLength.getOffset() + nbReceivedBytes);
	    	} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		} 
	    
		byte[] size =_msgLength.getBytes();
	    int length = ((size[0] & 0xFF) << 24) |
                ((size[1] & 0xFF) << 16) |
                ((size[2] & 0xFF) << 8) |
                (size[3] & 0xFF);
	    
	    _msg = new Message(length);
	    
	    nbReceivedBytes = 0;
		while(nbReceivedBytes < length) {
			try {
				nbReceivedBytes += _channel.read(_msg.getBytes(), _msg.getOffset(), length- _msg.getOffset());
				_msg.setOffset(_msg.getOffset() + nbReceivedBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		_listener.received(_msg.getBytes());
		
		return true;
	}

	@Override
	public void close() {
		_channel.disconnect();
		
	}

	@Override
	public boolean closed() {
		return _closed;
	}
	
	private void resetMessageLength() {
		_msgLength = new Message(4);
	}
	
	private void resetMessage() {
		_msg = null;
	}
	
	public void setMsq(MessageQueue mq) {
		_rmq = mq;
	}

}
