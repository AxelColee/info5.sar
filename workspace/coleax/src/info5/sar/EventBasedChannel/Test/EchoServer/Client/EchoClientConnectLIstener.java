package info5.sar.EventBasedChannel.Test.EchoServer.Client;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;

/**
 * EchoClientConnectLIstener is an implementation of the IConnectListener interface.
 * It handles the connection event for an EchoClient and manages the sending of messages
 * to the server upon connection.
 * 
 * This class generates a specified number of random messages and sends them to the server
 * through the provided channel.
 * 
 * <p>Constructor:</p>
 * <ul>
 *  <li>{@link #EchoClientConnectLIstener(int)}: Initializes the listener with the specified number of messages per client.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *  <li>{@link #connected(IChannel)}: Called when the client is connected to the server. Sends the messages to the server.</li>
 * <li>{@link #refused()}: Called when the connection is refused. Throws an exception indicating the connection was refused.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 * <li>{@code _bytes}: List of byte arrays to be sent to the server.</li>
 * <li>{@code _nbMessagePerClient}: Number of messages to be sent per client.</li>
 * </ul>
 */
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
			channel.write(_bytes.get(index));
		}
	}

	@Override
	public void refused() {
		throw new IllegalStateException("Connection refused");
	}

}
