package info5.sar.EventBasedChannel.Abstract;

import info5.sar.ThreadedChannel.Exception.DisconnectedException;

public interface IChannel {

	public abstract int read(byte[] bytes, int offset, int length) throws DisconnectedException;
	public abstract int write(byte[] bytes, int offset, int length) throws DisconnectedException;
	public abstract void disconnect();
	public abstract boolean disconnected();
	
}
