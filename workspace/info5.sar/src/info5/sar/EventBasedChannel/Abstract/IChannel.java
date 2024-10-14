package info5.sar.EventBasedChannel.Abstract;

public interface IChannel {

	public abstract void setListener(IListener listener);
	public abstract int read(byte[] bytes, int offset, int length);
	public abstract int write(byte[] bytes, int offset, int length);
	public abstract void disconnect();
	public abstract boolean disconnected();
	
}
