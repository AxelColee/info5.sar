package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IListener;
import info5.sar.EventBasedChannel.Abstract.ITask;

public class EchoClientListener implements IListener{
	
	private byte[] _bytes;
	private IChannel _channel;
	
	public EchoClientListener(byte[] bytes, IChannel channel) {
		_bytes = bytes;
	}

	@Override
	public void received(byte[] bytes) {
		
		_channel.disconnect();
	
		for(int i = 0; i < _bytes.length; i++){
			assert(_bytes[i] == bytes[i]) : "Data recieved different from the one sent : " + i;
		}	
	}

	@Override
	public void closed() {
		assert(_channel.disconnected() == true);
	}

	@Override
	public void sent(byte[] bytes) {
		
		byte[] answer = new byte[bytes.length];
		new ITask().post(() -> _channel.read(answer, 0, answer.length));		
	}

}
