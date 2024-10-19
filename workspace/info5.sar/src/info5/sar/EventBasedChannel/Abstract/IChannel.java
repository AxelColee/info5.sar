package info5.sar.EventBasedChannel.Abstract;

public interface IChannel {

	public abstract void setListener(IChannelListener listener);
	public abstract boolean read(byte[] bytes);
	public abstract boolean write(byte[] bytes);
	public abstract void disconnect();
	public abstract boolean disconnected();
	
}
