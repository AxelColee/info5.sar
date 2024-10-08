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
	private Message _sizeSent;
	
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
	
	public boolean _send(Message message){
	    int nbSentBytes = 0;
	    int blockSize = 16;
	    
	    
	    if(_sizeSent == null) {
	    	int length = message.getLength();
	        Message size = new Message(4);
	        size.getBytes()[0] = (byte) ((length & 0xFF000000) >> 24);
	        size.getBytes()[1] = (byte) ((length & 0x00FF0000) >> 16);
	        size.getBytes()[2] = (byte) ((length & 0x0000FF00) >> 8);
	        size.getBytes()[3] = (byte) (length & 0x000000FF);
	    	_sizeSent = size;
	    }
        
        try {
        	if(_sizeSent.getOffset() < 4) {
        		nbSentBytes += _channel.write(_sizeSent.getBytes(), 0, 4);
        		_sizeSent.setOffset(_sizeSent.getOffset() + nbSentBytes);
        		
				return false;

        	}			

		} catch (DisconnectedException e) {
			return false;
		}
        
        nbSentBytes = 0;
        
        int remaining = message.getLength() - message.getOffset();
        int toSend = Math.min(blockSize, remaining);

        
        if(message.getOffset() < message.getLength()) {
            try {
				nbSentBytes += _channel.write(message.getBytes(), message.getOffset(), toSend);
	            message.setOffset(message.getOffset()+ nbSentBytes);
            } catch (DisconnectedException e) {
				return false;
			}
        }


        if(message.getOffset() < message.getLength()) {
        	return false;
        }
        
        _sizeSent = null;
        
	    _listener.sent(message);
	    return true;
	    
	}
	
	public boolean _receive() {
	    int nbReceivedBytes = 0;
	    int blockSize = 16;

	    if (_msgLength.getOffset() < 4) {
	        try {
	            nbReceivedBytes += _channel.read(_msgLength.getBytes(), _msgLength.getOffset(), 4 - _msgLength.getOffset());
	            _msgLength.setOffset(_msgLength.getOffset() + nbReceivedBytes);
	                return false;
	        } catch (DisconnectedException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    nbReceivedBytes = 0;

	    if (_msg == null) {
	        byte[] sizeBytes = _msgLength.getBytes();
	        int length = ((sizeBytes[0] & 0xFF) << 24) |
	                     ((sizeBytes[1] & 0xFF) << 16) |
	                     ((sizeBytes[2] & 0xFF) << 8) |
	                     (sizeBytes[3] & 0xFF);

	        _msg = new Message(length);
	    }

	    if (_msg.getOffset() < _msg.getLength()) {
	        int remaining = _msg.getLength() - _msg.getOffset(); 
	        int toReceive = Math.min(blockSize, remaining); // Recevoir par blocs de 16 octets ou moins si la taille restante est plus petite

	        try {
	            nbReceivedBytes += _channel.read(_msg.getBytes(), _msg.getOffset(), toReceive);
	            _msg.setOffset(_msg.getOffset() + nbReceivedBytes);

	            if (_msg.getOffset() < _msg.getLength()) {
	                // Si tous les octets du bloc n'ont pas été reçus, retour false
	                return false;
	            }
	        } catch (DisconnectedException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    


	    // Si on arrive ici, le message entier a été reçu
	    _listener.received(_msg.getBytes());
	    
	    _msgLength = new Message(4);
	    _msg = null;
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
