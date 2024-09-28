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
				nbSentBytes += _channel.write(bytes, offset + nbSentBytes, length-nbSentBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	synchronized public byte[] receive() {
		int nbReceivedBytes = 0;
		
		byte[] size = new byte[4];
	    
		while (nbReceivedBytes < 4) {
	    	try {
	    		nbReceivedBytes = _channel.read(size, 0 + nbReceivedBytes, 4 - nbReceivedBytes);
			} catch (DisconnectedException e) {
				e.printStackTrace();
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
