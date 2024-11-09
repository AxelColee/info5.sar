package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IBroker;

/**
 * EchoServerAcceptListener is an implementation of the IAcceptListener interface.
 * It handles the acceptance of new client connections and sets up the necessary
 * channel listeners and message reading for each client.
 * 
 * <p>This class is responsible for:
 * <ul>
 *   <li>Incrementing the client connection counter upon acceptance.</li>
 *   <li>Setting a new EchoServerChannelListener for the accepted channel.</li>
 *   <li>Initiating the reading of a specified number of messages from the channel.</li>
 *   <li>Unbinding the broker from the port if the number of connected clients reaches the specified limit.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *  <li>{@code _broker}: The broker used to manage client connections.</li>
 * <li>{@code _nbClient}: The number of clients to be handled by the server.</li>
 * <li>{@code _nbMessagePerClient}: The number of messages to be handled per client.</li>
 * <li>{@code _counter}: Counter to keep track of the number of connected clients.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #accepted(IChannel)}: Called when a new client connection is accepted. Sets up the channel listener and initiates message reading.</li>
 * </ul>
 * 
 */
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

		for (int i = 0; i < _nbMessagePerClient; i++) {
			channel.read(new byte[360]);
		}
		
		if(_counter >= _nbClient) {
			_broker.unbind(80);
		}

	}

}
