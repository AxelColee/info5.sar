package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerChannelListener implements IChannelListener{
	
	private IChannel _channel;
	private static int _cpt = 0;
	
	public EchoServerChannelListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void read(byte[] bytes) {
		new Task().post(() -> _channel.write(bytes));
	}

	@Override
	public void disconnected() {
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
		
		if(_cpt++ >= 2) {
			System.out.println("Server Passed");
		}
	}

	@Override
	public void wrote(byte[] bytes) {
		_channel.disconnect();
	}
}
