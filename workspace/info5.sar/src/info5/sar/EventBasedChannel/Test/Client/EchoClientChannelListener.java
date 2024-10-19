package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientChannelListener implements IChannelListener {
	
	private byte[] _bytes;
	private IChannel _channel;
	
	public EchoClientChannelListener(byte[] bytes, IChannel channel) {
		_bytes = bytes;
		_channel = channel;
	}

	@Override
	public void read(byte[] bytes) {
		
		for(int i = 0; i < _bytes.length; i++){
			if(_bytes[i] != bytes[i]) 
				System.err.println("Data recieved different from the one sent : " + i);
		}
		
		_channel.disconnect();
	}

	@Override
	public void disconnected() {
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
		
		System.out.println("Client Passed");

	}
	
	@Override
	public void wrote(byte[] bytes) {
		byte[] answer = new byte[bytes.length];
		new Task().post(() -> _channel.read(answer));	

	}

}
