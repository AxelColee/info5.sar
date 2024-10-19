package info5.sar.EventBasedChannel.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientConnectLIstener implements IConnectListener{
	
	@Override
	public void connected(IChannel channel) {
		
		byte[] bytes = UUID.randomUUID().toString().repeat(10).getBytes();
		
		channel.setListener(new EchoClientChannelListener(bytes, channel));
		
		new Task().post(() -> channel.write(bytes));
		
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
