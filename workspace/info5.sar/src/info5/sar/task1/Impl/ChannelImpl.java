package info5.sar.task1.Impl;

import info5.sar.task1.Channel;

public class ChannelImpl extends Channel{
	
	private boolean _disconnected;
	private CircularBuffer _in, _out;
	
	public ChannelImpl(CircularBuffer in, CircularBuffer out) {
		this._in = in;
		this._out = out;
		this._disconnected = false;
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws DisconnectedException {
	    if (_disconnected) {
	        throw new DisconnectedException("Channel is disconnected");
	    }

	    int bytesRead = 0;
	    try {
	        for (int i = 0; i < length; i++) {
	            synchronized (_in) {  
	                while (_in.empty()) {
	                    _in.wait(); 
	                }

	                bytes[offset + i] = _in.pull(); 
	                bytesRead++;

	                _in.notifyAll(); 
	            }
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    return bytesRead;
	}

	@Override
	public int write(byte[] bytes, int offset, int length) throws DisconnectedException {
	    if (_disconnected) {
	        throw new DisconnectedException("Channel is disconnected");
	    }

	    int bytesWritten = 0;
	    try {
	        for (int i = 0; i < length; i++) {
	            synchronized (_out) {  
	                while (_out.full()) {
	                    _out.wait();  
	                }

	                _out.push(bytes[offset + i]);
	                bytesWritten++;

	                _out.notifyAll(); 
	            }
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }

	    return bytesWritten;
	}

	@Override
	public void disconnect() {
		this._disconnected = true;
	}

	@Override
	public boolean disconnected() {
		return this._disconnected;
	}
	
	

}
