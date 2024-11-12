package info5.sar.EventBasedChannel.Abstract;

/**
 * The {@code IChannelListener} interface provides methods to handle events related to 
 * channel operations such as disconnection, reading, and writing of data.
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #disconnected()}: Called when the channel is disconnected.</li>
 * <li>{@link #read(byte[])}: Called when data is read from the channel.</li>
 * <li>{@link #wrote(byte[])}: Called when data is written to the channel.</li>
 * </ul>
 * 
 */
public interface IChannelListener {
	
	/**
	 * Called when the channel is disconnected.
	 */
	public void disconnected();

	/**
	 * Called when data is read from the channel.
	 * @param bytes The data read from the channel.
	 */
	public void read(byte[] bytes);

	/**
	 * Called when data is written to the channel.
	 * @param bytes The data written to the channel.
	 */
	public void wrote(byte[] bytes);

}
