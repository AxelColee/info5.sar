package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IListener;

public class EchoServerListener implements IListener{
	
	private IChannel _channel;
	
	public EchoServerListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void received(byte[] bytes) {
		_channel.write(bytes, 0, bytes.length);
	}

	@Override
	public void closed() {
		assert(_channel.disconnected() == true);
	}

	@Override
	public void sent(byte[] bytes) {
		_channel.disconnect();
	}

}
