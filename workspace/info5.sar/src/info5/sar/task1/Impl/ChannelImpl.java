package info5.sar.task1.Impl;

import info5.sar.task1.Channel;

public class ChannelImpl extends Channel{
	
	
	public ChannelImpl _rch;
	private boolean _disconnected;
	public boolean _dangling;
	public CircularBuffer _in, _out;
	
	public ChannelImpl() {
		this._in = new CircularBuffer(64);
		this._disconnected = false;
		this._dangling = false;
	}

	
	
	public int read(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (_disconnected) {
			throw new DisconnectedException("Channel is locally disconnected");
		}
		int bytesRead = 0;
		try {
			while (bytesRead == 0) {
				if (_in.empty()) {
					synchronized (_in) {
						while (_in.empty()) {
							if (_dangling || _disconnected)
								throw new DisconnectedException("Channel is remotely disconnected");
							try {
								_in.wait();
							} catch (InterruptedException e) {
								// Do nothing
							}
						}
					}
				}
				
				while (bytesRead < length && !_in.empty()) {				
					byte value = _in.pull();
					bytes[offset + bytesRead] = value;
					bytesRead++;
				}
				
				if (bytesRead != 0) {
					synchronized (_in) {
						_in.notify();
					}
				}
			}
			
		} catch (DisconnectedException e) {
			if (!_disconnected) {
				_disconnected = true;
				synchronized (_out) {
					_out.notifyAll();
				}
			}
			throw e;
		}
		return bytesRead;
	}
	
	
	public int write(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (_disconnected) {
			throw new DisconnectedException("Channel is locally disconnected");
		}
		int bytesWritten = 0;
		
		while (bytesWritten == 0) {
			if (_out.full()) {
				synchronized (_out) {
					while (_out.full()) {
						if (_disconnected)
							throw new DisconnectedException("Channel is remotely disconnected");
						if (_dangling) {
							return length;
						}
						try {
							_out.wait();
						} catch (InterruptedException e) {
							// Do nothing
						}
					}
				}
			}

			while (bytesWritten < length && !_out.full()) {
				byte value = bytes[offset + bytesWritten];
				_out.push(value);
				bytesWritten++;
			}

			if (bytesWritten != 0) {
				synchronized (_out) {
					_out.notify();
				}
			}
		}
		
		return bytesWritten;
	}

	@Override
	public boolean disconnected() {
		return this._disconnected;
	}
	
	public void disconnect() {
		synchronized(this) {
			if(disconnected())
				return;
			
			_disconnected = true;
			
			_rch._dangling = true;
			
			synchronized(_out) {
				_out.notifyAll();
			}
			
			synchronized(_in) {
				_in.notifyAll();
			}
		}
	}
	
	

}
