package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IWriteListener;

public class EchoServerWriteListener implements IWriteListener{
	
	private IChannel _channel;
	
	public EchoServerWriteListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void wrote(byte[] bytes) {
		_channel.disconnect();
		
		System.out.println("Server Passed");
	}

}
