package info5.sar.EventBasedChannel.Impl;

import java.util.Queue;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IDisconnectListener;
import info5.sar.EventBasedChannel.Abstract.IReadListener;
import info5.sar.EventBasedChannel.Abstract.IWriteListener;
import info5.sar.ThreadedChannel.Impl.CircularBuffer;

public class Channel implements IChannel{
	
	private static final int MAX_BUFFER_SIZE = 1024;
	
	public Channel _rch;
	private boolean _disconnected;
	public boolean _dangling;
	public CircularBuffer _in, _out;
	private IReadListener _readListener;
	private IDisconnectListener _disconnectListener;
	private Queue<byte[]> _writeBuffer, _readBuffer;
	

	@Override
	public void setReadListener(IReadListener listener) {
		_readListener = listener;
	}

	@Override
	public void setDisconnectListener(IDisconnectListener listener) {
		_disconnectListener = listener;
	}

	@Override
	public boolean read(byte[] bytes) {
		if(getReadBufferSize() + bytes.length > MAX_BUFFER_SIZE) {
			return false;
		}
		if(_readBuffer.isEmpty()) {
			_read(bytes, 0, bytes.length);
		}
		_readBuffer.add(bytes);
		return true;
	}
	
	private void _read(byte[] bytes, int offset, int length) {
		
		if(_disconnected) {
			return;
		}
		
		if(_in.empty()) {
			new Task().post(() -> _read(bytes, offset, length));
			return;
		}
		
		int bytesRead = 0;
		while (bytesRead < length - offset && !_in.empty()) {				
			byte value = _in.pull();
			bytes[offset + bytesRead] = value;
			bytesRead++;
		}
		
		if(offset >= length) {
			_readListener.read(_readBuffer.poll());
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
	public boolean write(byte[] bytes, IWriteListener listener) {
		if(getWriteBufferSize() + bytes.length > MAX_BUFFER_SIZE) {
			return false;
		}
		if(_writeBuffer.isEmpty()) {
			_write(bytes, 0, bytes.length, listener);
		}
		_writeBuffer.add(bytes);
		return true;
	}
	
	private void _write(byte[] bytes, int offset, int length, IWriteListener listener) {
		
		if(_dangling || _disconnected) {
			return;
		}
		
		if(_out.full()) {
			new Task().post(() -> _write(bytes, offset, length, listener));
			return;
		}
		
		int bytesWritten = 0;
		while (bytesWritten < length - offset && !_out.full()) {				
			_out.push(bytes[offset + bytesWritten++]);
		}
		
		if(offset >= length) {
			listener.wrote(_writeBuffer.poll());
			byte[] nextBytes = _writeBuffer.peek();
			if(nextBytes != null) {
				new Task().post(() -> _read(nextBytes, 0, nextBytes.length));
			}
			return;
		}
		
	    final int updatedOffset = offset + bytesWritten;
		new Task().post(() -> _read(bytes, updatedOffset, length));
	}

	@Override
	public void disconnect() {
		_disconnected = true;
		
		if(_disconnectListener == null) {
			throw new IllegalStateException("DsiconnectListener not set");
		}
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

}
