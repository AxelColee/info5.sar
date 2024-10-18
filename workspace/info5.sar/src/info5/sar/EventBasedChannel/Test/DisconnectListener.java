package info5.sar.EventBasedChannel.Test;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IDisconnectListener;

public class DisconnectListener implements IDisconnectListener {
	
	private IChannel _channel;
	
	public DisconnectListener(IChannel channel) {
		_channel = channel;
	}

	@Override
	public void disconnected() {
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
	}

}
