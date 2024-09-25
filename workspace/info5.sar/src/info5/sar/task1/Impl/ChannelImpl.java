package info5.sar.task1.Impl;

import java.security.DrbgParameters.NextBytes;

import info5.sar.task1.Channel;

public class ChannelImpl extends Channel{
	
	private boolean _disconnected;
	private CircularBuffer _in, _out;
	
	public ChannelImpl(CircularBuffer in, CircularBuffer out) { // Creation par défaut du buffer in , dans une methode connect avec l'autre channel lier les deux channels en croisés. et donc ne pas le mettre dans le constructeur
		this._in = in;
		this._out = out;
		this._disconnected = false;
	}

	
	
	public int rendCorrection(byte[] bytes, int offset, int length)
	{
	    if (_disconnected) {
	        throw new DisconnectedException("Channel is disconnected");
	    }
	    int nbytes = 0;
	    try {
		    while(nbytes == 0) {
		    	if(_in.empty()) {
		    		if(_disconnected || dangling) {
		    			throw new DisconnectedException("Channel is disconnected");
		    		}
		    		try {
		    			_in.wait();
		    		}catch (Exception e) {
						//ntdh
					}
		    	}
		    }
		    
		    while(nbytes < length && !_in.empty()) {
		    	byte val = _in.pull();
		    	bytes[offset+nbytes]= val;
		    	nbytes++;
		    }
		    if(nbytes != 0) {
		    	synchronized (_in) {
					_in.notify();
				}
		    }
	    	
	    }catch (DisconnectedException e) {
			if(!_disconnected) {
				_disconnected = true;
				synchronized (_out) {
					_out.notifyAll();
					
				}
			}
			throw e;
		}

	    return nbytes;
		
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
	
	
	public int writeCorrection(byte[] bytes, int offset, int length) throws DisconnectedException {
	    if (_disconnected) {
	        throw new DisconnectedException("Channel is disconnected");
	    }
	    int nbytes = 0;
	    while(nbytes == 0) {
	    	if(_out.full()) {
	    		if(disconnected()) {
	    			throw new DisconnectedException("");	
	    		}
	    		if(dangling) {
	    			return length;
	    		}
	    		try {
	    			_out.wait();
	    		}catch (Exception e) {
					// ntdh
				}
	    	}
	    }
	    while(nbytes < length && !_out.full()) {
	    	byte val = bytes[offset + nbytes];
	    	_out.push(val);
	    	nbytes++;
	    }
	    if(nbytes != 0) {
	    	synchronized (_out) {
	    		_out.notify();
			}
	    }
	    return nbytes;
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
	
	public void disconnectCorrection() {
		synchronized(this) {
			if(disconnected())
				return;
			
			_disconnected = true;
			
			//rch.dangling = true;
			
			synchronized(_out) {
				_out.notifyAll();
			}
			
			synchronized(_in) {
				_in.notifyAll();
			}
		}
	}
	
	

}
