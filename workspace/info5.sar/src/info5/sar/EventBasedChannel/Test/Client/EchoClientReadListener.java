package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IDisconnectListener;
import info5.sar.EventBasedChannel.Abstract.IReadListener;
import info5.sar.EventBasedChannel.Abstract.ITask;

public class EchoClientReadListener implements IReadListener{
	
	private byte[] _bytes;
	private IChannel _channel;
	
	public EchoClientReadListener(byte[] bytes, IChannel channel) {
		_bytes = bytes;
	}

	@Override
	public void read(byte[] bytes) {
		_channel.disconnect();
		
		for(int i = 0; i < _bytes.length; i++){
			assert(_bytes[i] == bytes[i]) : "Data recieved different from the one sent : " + i;
		}	
		
	}

}
