package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListner;

public class EchoClientConnectLIstener implements IConnectListner{
	
	private IBroker _broker;
	
	public EchoClientConnectLIstener(IBroker broker) {
		_broker = broker;
	}

	@Override
	public void connected(IChannel channel) {
		
		byte[] bytes = "Hello from client".getBytes();

		channel.setListener(new EchoClientListener(bytes, channel));
		
		channel.write(bytes, 0,17);
		
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
