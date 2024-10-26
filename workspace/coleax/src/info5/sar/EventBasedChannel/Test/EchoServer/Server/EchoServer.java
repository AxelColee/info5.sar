package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IBroker;

public class EchoServer implements Runnable{

	private IBroker _broker;
	private int _nbClient;
	private int _nbMessagePerClient;
	
	public EchoServer(IBroker broker, int nbClient, int nbMessagePerClient) {
		_broker = broker;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void run() {
		 _broker.bind(80, new EchoServerAcceptListener(_broker, _nbClient, _nbMessagePerClient));
	}

}
