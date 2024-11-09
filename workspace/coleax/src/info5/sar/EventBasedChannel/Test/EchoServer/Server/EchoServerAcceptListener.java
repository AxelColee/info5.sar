package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IBroker;

public class EchoServerAcceptListener implements IAcceptListener{
	
	private IBroker _broker;
	private int _nbClient;
	private int _nbMessagePerClient;
	private int _counter;
	
	public EchoServerAcceptListener(IBroker broker, int nbClient, int nbMessagePerClient) {
		_broker = broker;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
		_counter = 0;
	}

	@Override
	public void accepted(IChannel channel) {
		
		_counter++;
		
		channel.setListener(new EchoServerChannelListener(channel, _nbClient, _nbMessagePerClient));

		for (int i = 0; i < _nbMessagePerClient; i++) {
			channel.read(new byte[360]);
		}
		
		if(_counter >= _nbClient) {
			_broker.unbind(80);
		}

	}

}
