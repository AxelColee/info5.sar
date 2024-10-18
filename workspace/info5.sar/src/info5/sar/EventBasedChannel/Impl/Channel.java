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
	private Queue<byte[]> _writeBuffer, readBuffer;
	

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
		if(_writeBuffer.isEmpty()) {
			_read(bytes, 0, bytes.length);
		}
		_writeBuffer.add(bytes);
		return true;
	}
	
	private void _read(byte[] bytes, int offset, int length) {
		
		if(_in.full()) {
			new Task().post(() -> _read(bytes, offset, length));
		}
		
		int bytesRead = 0;
		while (bytesRead < length - offset && !_in.empty()) {				
			byte value = _in.pull();
			bytes[offset + bytesRead] = value;
			bytesRead++;
		}
		
		if(offset == length) {
			_readListener.read(_writeBuffer.poll());
			new Task().post(() -> _read(bytes, offset, length));
		}
	}

	@Override
	public boolean write(byte[] bytes, IWriteListener listenner) {
		// TODO Auto-generated method stub
		return false;
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
		return getWriteBufferSize();
	}

}
