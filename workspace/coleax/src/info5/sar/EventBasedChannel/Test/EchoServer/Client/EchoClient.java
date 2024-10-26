package info5.sar.EventBasedChannel.Test.EchoServer.Client;

import info5.sar.EventBasedChannel.Abstract.IBroker;

public class EchoClient implements Runnable{

	private IBroker _broker;
	private int _nbMessagePerClient;
	
	public EchoClient(IBroker broker, int nbMessagePerClient) {
		_broker = broker;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void run() {
		_broker.connect("serverBroker", 80, new EchoClientConnectLIstener(_nbMessagePerClient));		
	}

}
