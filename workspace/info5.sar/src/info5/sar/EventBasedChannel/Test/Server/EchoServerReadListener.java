package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IReadListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerReadListener implements IReadListener {
	
private IChannel _channel;
	
	public EchoServerReadListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void read(byte[] bytes) {
		new Task().post(() -> _channel.write(bytes, new EchoServerWriteListener(_channel)));
	}

}
