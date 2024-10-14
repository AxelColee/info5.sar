package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IBroker;

public class EchoClient implements Runnable{

	private IBroker _broker;
	
	public EchoClient(IBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		_broker.connect("serverBroker", 80, new EchoClientConnectLIstener(_broker));		
	}

}
