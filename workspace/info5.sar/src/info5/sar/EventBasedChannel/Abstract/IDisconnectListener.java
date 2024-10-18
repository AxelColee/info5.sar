package info5.sar.EventBasedChannel.Abstract;

public interface IDisconnectListener {
	
	public void received(byte[] bytes);
	public void disconnected();
	public void sent(byte[] bytes);

}
