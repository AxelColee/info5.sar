package info5.sar.EventBasedMessageQueue.Impl;

public class Message {
	
	private byte[] _bytes;
    private int _offset;
    private int _length;
	
	public Message(byte[] bytes, int offset, int length) {
		_bytes = bytes;
		_offset = offset;
		_length = length;
	}
	
	public int getOffset() {
		return _offset;
	}
	
	public int getLength() {
		return _length;
	}

	public void setOffset(int offset) {
		this._offset = offset;
	}


	public void setLength(int length) {
		this._length = length;
	}

	public byte[] getBytes() {
		return _bytes;
	}
	
	@Override
	public String toString() {
		return new String(_bytes);
	}
	
	public byte getByteAt(int i) {
		return _bytes[i];
	}
}

