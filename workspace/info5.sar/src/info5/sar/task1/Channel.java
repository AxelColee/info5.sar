package info5.sar.task1;

import info5.sar.task1.Impl.DisconnectedException;

public abstract class Channel{

	public abstract int read(byte[] bytes, int offset, int length) throws DisconnectedException;

	public abstract int write(byte[] bytes, int offset, int length) throws DisconnectedException;

	public abstract void disconnect();

	public abstract boolean disconnected();

    
}
