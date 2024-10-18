package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IReadListener;

public class EchoServerReadListener implements IReadListener {
	
private IChannel _channel;
	
	public EchoServerReadListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void read(byte[] bytes) {
		_channel.write(bytes, 0, bytes.length);
	}

}
