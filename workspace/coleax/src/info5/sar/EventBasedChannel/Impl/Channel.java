package info5.sar.EventBasedChannel.Impl;

import java.util.LinkedList;
import java.util.Queue;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;

/**
 * The Channel class implements the IChannel interface and provides methods for reading and writing data
 * through a circular buffer. It supports setting a listener for handling read and write events, and 
 * managing the connection state.
 * 
 * <p>Fields:</p>
 * <ul>
 *  <li>{@code _rch} - The remote channel for communication.</li>
 * <li>{@code _disconnected} - A flag indicating if the channel is disconnected.</li>
 * <li>{@code _dangling} - A flag indicating if the channel is in a dangling state.</li>
 * <li>{@code _in} - The circular buffer for incoming data.</li>
 * <li>{@code _out} - The circular buffer for outgoing data.</li>
 * <li>{@code _listener} - The listener for the channel.</li>
 * <li>{@code _writeBuffer} - A queue for storing data to be written.</li>
 * <li>{@code _readBuffer} - A queue for storing data to be read.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *  <li>{@link #setListener(IChannelListener)} - Sets the listener for the channel.</li>
 *  <li>{@link #getListener()} - Gets the listener for the channel.</li>
 *  <li>{@link #read(byte[])} - Reads data from the channel.</li>
 * 	<li>{@link #write(byte[])} - Writes data to the channel.</li>
 * 	<li>{@link #disconnect()} - Disconnects the channel.</li>
 * 	<li>{@link #disconnected()} - Checks if the channel is disconnected.</li>
 * </ul>
 */
public class Channel implements IChannel{
	
	private static final int MAX_BUFFER_SIZE = 60000;
	
	public Channel _rch;
	private boolean _disconnected;
	public boolean _dangling;
	public CircularBuffer _in, _out;
	private IChannelListener _listener;
	private Queue<byte[]> _writeBuffer, _readBuffer;
	
	public Channel() {
		_disconnected = false;
		_dangling = false;
		_in = new CircularBuffer(64);
		_writeBuffer = new LinkedList<byte[]>();
		_readBuffer = new LinkedList<byte[]>();
	}
	

	@Override
	public void setListener(IChannelListener listener) {
		_listener = listener;
	}

	@Override
	public boolean read(byte[] bytes) {
		
		if(_listener == null) {
			throw new IllegalStateException("Listener not set");
		}
		
		if(getReadBufferSize() + bytes.length > MAX_BUFFER_SIZE) {
			return false;
		}
		
		_readBuffer.add(bytes);

		if(_readBuffer.size() <= 1) {
			_read(bytes, 0, bytes.length);
		}
		return true;
	}
	
	private void _read(byte[] bytes, int offset, int length) {
		
		if(_disconnected) {
			return;
		}
		
		if(_in.empty()) {
			if(!_dangling) {
				new Task().post(() -> _read(bytes, offset, length));
			}else {
				_readBuffer.clear();
			}
			return;
		}
		
		int bytesRead = 0;
		while (bytesRead < length - offset && !_in.empty()) {				
			byte value = _in.pull();
			bytes[offset + bytesRead] = value;
			bytesRead++;
		}
		
		if(bytesRead >= length - offset) {
			_listener.read(_readBuffer.poll());
			byte[] nextBytes = _readBuffer.peek();
			if(nextBytes != null) {
				new Task().post(() -> _read(nextBytes, 0, nextBytes.length));
			}
			return;
		}
		
	    final int updatedOffset = offset + bytesRead;
		new Task().post(() -> _read(bytes, updatedOffset, length));
		
	}

	@Override
	public boolean write(byte[] bytes) {
		
		if(_listener == null) {
			throw new IllegalStateException("Listener not set");
		}
		
		if(getWriteBufferSize() + bytes.length > MAX_BUFFER_SIZE) {
			return false;
		}
		
		_writeBuffer.add(bytes);

		if(_writeBuffer.size() <= 1) {
			_write(bytes, 0, bytes.length);
		}
		return true;
	}
	
	private void _write(byte[] bytes, int offset, int length) {
		
		if( _disconnected) {
			return;
		}
		
		if(_dangling) {
			_listener.wrote(_writeBuffer.poll());
			byte[] nextBytes = _writeBuffer.peek();
			if(nextBytes != null) {
				new Task().post(() -> _write(nextBytes, 0, nextBytes.length));
			}
			return;
		}
		
		if(_out.full()) {
			new Task().post(() -> _write(bytes, offset, length));
			return;
		}
		
		int bytesWritten = 0;
		while (bytesWritten < length - offset && !_out.full()) {				
			_out.push(bytes[offset + bytesWritten++]);
		}
		
		if(bytesWritten >= length - offset) {
			_listener.wrote(_writeBuffer.poll());
			byte[] nextBytes = _writeBuffer.peek();
			if(nextBytes != null) {
				new Task().post(() -> _write(nextBytes, 0, nextBytes.length));
			}
			return;
		}
		
	    final int updatedOffset = offset + bytesWritten;
		new Task().post(() -> _write(bytes, updatedOffset, length));
	}

	@Override
	public void disconnect() {
		_disconnected = true;
		
		_rch._dangling = true;
		
		if(_listener == null) {
			throw new IllegalStateException("Listener not set");
		}
		
		_listener.disconnected();
	}

	@Override
	public boolean disconnected() {
		return _disconnected;
	}
	
	private int getBufferSize(Queue<byte[]> buffer) {
		int sum = 0;
		for(byte[] bytes : buffer) {
			sum += bytes.length;
		}
		return sum;
	}
	
	private int getWriteBufferSize() {
		return getBufferSize(_writeBuffer);
	}
	
	private int getReadBufferSize() {
		return getBufferSize(_readBuffer);
	}


	@Override
	public IChannelListener getListener() {
		return _listener;
	}

}
