package info5.sar.EventBasedChannel.Abstract;

/**
 * The IChannelListener interface provides methods to handle events related to 
 * channel operations such as disconnection, reading, and writing of data.
 */
public interface IChannelListener {
	
	public void disconnected();
	public void read(byte[] bytes);
	public void wrote(byte[] bytes);

}
