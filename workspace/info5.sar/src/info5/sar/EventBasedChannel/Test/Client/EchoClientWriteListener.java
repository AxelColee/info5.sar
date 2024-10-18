package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IWriteListener;

public class EchoClientWriteListener implements IWriteListener {
	
	private IChannel _channel;
	
	public EchoClientWriteListener( IChannel channel) {
		_channel = channel;
	}

	@Override
	public void wrote(byte[] bytes) {
		byte[] answer = new byte[bytes.length];
		_channel.read(answer, 0, answer.length);	

	}

}
