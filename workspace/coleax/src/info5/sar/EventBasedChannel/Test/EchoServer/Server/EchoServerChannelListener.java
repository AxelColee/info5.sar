package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerChannelListener implements IChannelListener{
	
	private IChannel _channel;
	private static int _clientCounter = 0;
	private int _messageCounter;
	private int _nbClient;
	private int _nbMessagePerClient;
	
	public EchoServerChannelListener(IChannel channel, int nbClient, int nbMessagePerClient) {
		_channel = channel;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
		_messageCounter = 0;
	}

	@Override
	public void read(byte[] bytes) {
		new Task().post(() -> _channel.write(bytes));
	}

	@Override
	public void disconnected() {
		_clientCounter++;
		
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
		
		if(_clientCounter >= _nbClient) {
			_clientCounter = 0;
			System.out.println("Server Passed");
		}
	}

	@Override
	public void wrote(byte[] bytes) {
		_messageCounter++;
		
		if(_messageCounter >= _nbMessagePerClient) {
			_channel.disconnect();
		}
	}
}
