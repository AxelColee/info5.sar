package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListner;
import info5.sar.EventBasedChannel.Test.DisconnectListener;

public class EchoClientConnectLIstener implements IConnectListner{
	
	@Override
	public void connected(IChannel channel) {
		
		byte[] bytes = "Hello from client".getBytes();

		channel.setReadListener(new EchoClientReadListener(bytes, channel));
		channel.setDisconnectListener(new DisconnectListener(channel));
		
		channel.write(bytes, 0,17);
		
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
