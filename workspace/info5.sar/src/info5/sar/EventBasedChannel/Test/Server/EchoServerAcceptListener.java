package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerAcceptListener implements IAcceptListener{

	@Override
	public void accepted(IChannel channel) {
		
		channel.setListener(new EchoServerChannelListener(channel));
		
		byte[] bytes1 = new byte[360];
		byte[] bytes2 = new byte[360];
		byte[] bytes3 = new byte[360];


		
		Task task = new Task();
		task.post(() -> channel.read(bytes1));
		task.post(() -> channel.read(bytes2));
		task.post(() -> channel.read(bytes3));

	}

}
