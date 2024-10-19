package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IWriteListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientWriteListener implements IWriteListener {
	
	private IChannel _channel;
	
	public EchoClientWriteListener( IChannel channel) {
		_channel = channel;
	}

	@Override
	public void wrote(byte[] bytes) {
		byte[] answer = new byte[bytes.length];
		new Task().post(() -> _channel.read(answer));	

	}

}
