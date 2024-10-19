package info5.sar.EventBasedChannel.Test.Client;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedChannel.Test.DisconnectListener;

public class EchoClientConnectLIstener implements IConnectListener{
	
	@Override
	public void connected(IChannel channel) {
		
		byte[] bytes = "Hello from client".getBytes();

		channel.setReadListener(new EchoClientReadListener(bytes, channel));
		channel.setDisconnectListener(new DisconnectListener(channel));
		
		new Task().post(() -> channel.write(bytes, new EchoClientWriteListener(channel)));
		
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
