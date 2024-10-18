package info5.sar.EventBasedChannel.Abstract;

public interface IChannel {

	public abstract void setReadListener(IReadListener listener);
	public abstract void setDisconnectListener(IDisconnectListener listener);
	public abstract boolean read(byte[] bytes, int offset, int length);
	public abstract boolean write(byte[] bytes, int offset, int length);
	public abstract void disconnect();
	public abstract boolean disconnected();
	
}
