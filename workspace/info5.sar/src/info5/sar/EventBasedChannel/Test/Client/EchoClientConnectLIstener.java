package info5.sar.EventBasedChannel.Test.Client;

import java.util.UUID;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientConnectLIstener implements IConnectListener{
	
	@Override
	public void connected(IChannel channel) {
		
		byte[] bytes1 = UUID.randomUUID().toString().repeat(10).getBytes(); bytes1[0] = 1;
		byte[] bytes2 = UUID.randomUUID().toString().repeat(10).getBytes(); bytes2[0] = 2;
		byte[] bytes3 = UUID.randomUUID().toString().repeat(10).getBytes(); bytes3[0] = 3;


		
		channel.setListener(new EchoClientChannelListener(bytes1, bytes2, bytes3, channel));
		
		new Task().post(() -> channel.write(bytes1));
		new Task().post(() -> channel.write(bytes2));
		new Task().post(() -> channel.write(bytes3));


		
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
