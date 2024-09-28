package info5.sar.MessageQueue.Impl;

import info5.sar.Channel.Abstract.Channel;
import info5.sar.Channel.Exception.DisconnectedException;

public class MessageQueue extends info5.sar.MessageQueue.Abstract.MessageQueue{
	
	private Channel _channel;
	
	MessageQueue(Channel channel){
		_channel=channel;
	}

	@Override
	synchronized public void send(byte[] bytes, int offset, int length) {
		
		int nbSentBytes = 0;

		byte[] size = new byte[1];
	    size[0] = (byte) length;
	    
	    do {
	    	try {
				nbSentBytes = _channel.write(size, 0, 1);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		} while (nbSentBytes < 1);
	    
		
		nbSentBytes = 0;
		while(nbSentBytes < length) {
			try {
				nbSentBytes += _channel.write(bytes, offset + nbSentBytes, length-nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	synchronized public byte[] receive() {
		int nbReceivedBytes = 0;
		
		byte[] size = new byte[1];
	    
	    do {
	    	try {
	    		nbReceivedBytes = _channel.read(size, 0, 1);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		} while (nbReceivedBytes < 1);
	    
	    int length = size[0];
	    byte [] message = new byte[length];
	    
	    nbReceivedBytes = 0;
		while(nbReceivedBytes < length) {
			try {
				nbReceivedBytes += _channel.read(message, 0 + nbReceivedBytes, length-nbReceivedBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		
		return message;
	}

	@Override
	public void close() {
		_channel.disconnect();
		
	}

	@Override
	public boolean closed() {
		return _channel.disconnected();
	}

}
