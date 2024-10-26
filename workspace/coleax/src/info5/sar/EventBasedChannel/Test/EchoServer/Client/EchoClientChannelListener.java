package info5.sar.EventBasedChannel.Test.EchoServer.Client;

import java.util.LinkedList;
import java.util.List;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientChannelListener implements IChannelListener {
	
	private List<byte[]> _bytes;
	private int _nbMessagePerClient;
	private IChannel _channel;
	private int _counter =0;
	
	public EchoClientChannelListener(IChannel channel, List<byte[]> bytes, int nbMessagePerClient) {
		_channel = channel;
		_bytes = bytes;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void read(byte[] bytes) {
		
		byte[] byteArrayReceived = _bytes.get(_counter);
		
		for(int i = 0; i < byteArrayReceived.length; i++){
			assert(bytes[i] == byteArrayReceived[i]) : "Data recieved different from the one sent : " + i;
		}	
		
		_counter++;

		if(_counter >= _nbMessagePerClient) {
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
