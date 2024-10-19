package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerAcceptListener implements IAcceptListener{

	@Override
	public void accepted(IChannel channel) {
		
		channel.setListener(new EchoServerChannelListener(channel));
		
		byte[] bytes = new byte[360];
		
		new Task().post(() -> channel.read(bytes));
	}

}
