package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;

/**
 * EchoServerChannelListener is an implementation of the IChannelListener interface.
 * It listens for events on a channel and performs actions based on those events.
 * 
 * <p>This class is responsible for handling read, write, and disconnect events on the channel.
 * It echoes back any received messages and manages client connections and message counts.</p>
 * 
 * <p>When the number of clients that have disconnected reaches the specified number of clients,
 * it prints "Server Passed" to the console. When the number of messages written by a client
 * reaches the specified number of messages per client, it disconnects the channel.</p>
 * @see IChannelListener
 * 
 * <p>Fields:</p>
 * <ul>
 *  <li>{@code _channel}: The channel associated with this listener.</li>
 * <li>{@code _clientCounter}: Counter to keep track of the number of clients that have disconnected.</li>
 * <li>{@code _messageCounter}: Counter to keep track of the number of messages written by a client.</li>
 * <li>{@code _nbClient}: The number of clients to be handled by the server.</li>
 * <li>{@code _nbMessagePerClient}: The number of messages to be handled per client.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #read(byte[])}: Called when data is read from the channel. Echoes the data back to the client.</li>
 * <li>{@link #disconnected()}: Called when the channel is disconnected. Increments the client counter and prints "Server Passed" if the number of clients has been reached.</li>
 * <li>{@link #wrote(byte[])}: Called when data is written to the channel. Increments the message counter and disconnects the channel if the number of messages has been reached.</li>
 * </ul>
 */
public class EchoServerChannelListener implements IChannelListener{
	
	private IChannel _channel;
	private static int _clientCounter = 0;
	private int _messageCounter;
	private int _nbClient;
	private int _nbMessagePerClient;
	
	public EchoServerChannelListener(IChannel channel, int nbClient, int nbMessagePerClient) {
		_channel = channel;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
		_messageCounter = 0;
	}

	@Override
	public void read(byte[] bytes) {
		_channel.write(bytes);
	}

	@Override
	public void disconnected() {
		_clientCounter++;
		
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
		
		if(_clientCounter >= _nbClient) {
			_clientCounter = 0;
			System.out.println("Server Passed");
		}
	}

	@Override
	public void wrote(byte[] bytes) {
		_messageCounter++;
		
		if(_messageCounter >= _nbMessagePerClient) {
			_channel.disconnect();
		}
	}
}
