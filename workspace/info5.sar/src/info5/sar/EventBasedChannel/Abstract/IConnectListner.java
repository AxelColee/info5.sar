package info5.sar.EventBasedChannel.Abstract;

public interface IConnectListner {
	public void connected(IChannel channel);
	public void refused();
}
