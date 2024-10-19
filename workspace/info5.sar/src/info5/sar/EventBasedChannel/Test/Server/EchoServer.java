package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IBroker;

public class EchoServer implements Runnable{

	private IBroker _broker;
	
	public EchoServer(IBroker broker) {
		_broker = broker;
	}

	@Override
	public void run() {
		 _broker.bind(80, new EchoServerAcceptListener());
	}

}
