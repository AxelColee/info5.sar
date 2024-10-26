package info5.sar.EventBasedChannel.Test.Client;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;
import info5.sar.EventBasedChannel.Impl.Task;

public class EchoClientConnectLIstener implements IConnectListener{
	
	private List<byte[]> _bytes;
	private int _nbMessagePerClient;
	
	public EchoClientConnectLIstener( int nbMessagePerClient) {
		_bytes = new LinkedList<byte[]>();
		_nbMessagePerClient = nbMessagePerClient;
	}
	
	@Override
	public void connected(IChannel channel) {
		
		for(int i = 0; i < _nbMessagePerClient; i++) {
			_bytes.add(UUID.randomUUID().toString().repeat(10).getBytes());
		}

		channel.setListener(new EchoClientChannelListener(channel, _bytes, _nbMessagePerClient));
		
		
		for(int i = 0; i < _nbMessagePerClient; i++) {
			final int index = i;
			new Task().post(() -> channel.write(_bytes.get(index)));

		}
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
