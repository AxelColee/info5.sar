package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientChannelListener implements IChannelListener {
	
	private byte[] _bytes1;
	private byte[] _bytes2;
	private byte[] _bytes3;


	private IChannel _channel;
	
	private int cpt =0;
	
	public EchoClientChannelListener(byte[] bytes1, byte[] bytes2, byte[] bytes3,IChannel channel) {
		_bytes1 = bytes1;
		_bytes2 = bytes2;
		_bytes3 = bytes3;
		_channel = channel;
	}

	@Override
	public void read(byte[] bytes) {
		
		if(bytes[0] == 1) {
			for(int i = 0; i < _bytes1.length; i++){
				if(_bytes1[i] != bytes[i]) 
					System.err.println("Data recieved different from the one sent : " + i);
			}
		}else if(bytes[0] == 2) {
			for(int i = 0; i < _bytes2.length; i++){
				if(_bytes2[i] != bytes[i]) 
					System.err.println("Data recieved different from the one sent : " + i);
			}
		
	}else if(bytes[0] == 3) {
		for(int i = 0; i < _bytes3.length; i++){
			if(_bytes3[i] != bytes[i]) 
				System.err.println("Data recieved different from the one sent : " + i);
		}
	}
		
		if(cpt++>=2) {
			_channel.disconnect();
		}
		
		
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
