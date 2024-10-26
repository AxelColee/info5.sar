package info5.sar.EventBasedChannel.Abstract;

public interface IConnectListener {
	public void connected(IChannel channel);
	public void refused();
}
