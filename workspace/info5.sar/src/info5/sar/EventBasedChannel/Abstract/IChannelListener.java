package info5.sar.EventBasedChannel.Abstract;

public interface IChannelListener {
	
	public void disconnected();
	public void read(byte[] bytes);
	public void wrote(byte[] bytes);

}
