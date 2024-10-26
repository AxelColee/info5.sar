package info5.sar.EventBasedChannel.Test.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoServerAcceptListener implements IAcceptListener{
	
	private IBroker _broker;
	private int _nbClient;
	private int _nbMessagePerClient;
	private int _counter;
	
	public EchoServerAcceptListener(IBroker broker, int nbClient, int nbMessagePerClient) {
		_broker = broker;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
		_counter = 0;
	}

	@Override
	public void accepted(IChannel channel) {
		
		_counter++;
		
		channel.setListener(new EchoServerChannelListener(channel, _nbClient, _nbMessagePerClient));
		
		Task task = new Task();
		for (int i = 0; i < _nbMessagePerClient; i++) {
			task.post(() -> channel.read(new byte[360]));
		}
		
		if(_counter >= _nbClient) {
			new Task().post(() -> _broker.unbind(80));
		}

	}

}
