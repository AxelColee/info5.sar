package info5.sar.EventBasedChannel.Abstract;

public interface IListener {
	
	public void received(byte[] bytes);
	public void closed();
	public void sent(byte[] bytes);

}
