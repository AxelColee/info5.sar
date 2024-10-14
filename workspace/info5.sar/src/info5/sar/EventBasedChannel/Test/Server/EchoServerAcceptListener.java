package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;

public class EchoServerAcceptListener implements IAcceptListener{

	@Override
	public void accepted(IChannel channel) {
		channel.setListener(new EchoServerListener(channel));
		
		byte[] bytes = new byte[17];
		
		channel.read(bytes, 0, 17);
	}

}
