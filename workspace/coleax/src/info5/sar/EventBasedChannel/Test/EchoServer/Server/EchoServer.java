package info5.sar.EventBasedChannel.Test.EchoServer.Server;

import info5.sar.EventBasedChannel.Abstract.IBroker;

/**
 * The EchoServer class implements the Runnable interface and represents a server
 * that handles echo requests from clients. It uses an IBroker to manage client connections
 * and messages.
 *
 * <p>Fields:</p>
 * <ul>
 *  <li>{@code _broker} - The broker used to manage client connections.</li>
 * <li>{@code _nbClient} - The number of clients to be handled by the server.</li>
 * <li>{@code _nbMessagePerClient} - The number of messages to be handled per client.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #run()} - Binds the server to a port and listens for client connections.</li>
 * </ul>
 * 
 */
public class EchoServer implements Runnable{

	private IBroker _broker;
	private int _nbClient;
	private int _nbMessagePerClient;
	
	public EchoServer(IBroker broker, int nbClient, int nbMessagePerClient) {
		_broker = broker;
		_nbClient = nbClient;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void run() {
		 _broker.bind(80, new EchoServerAcceptListener(_broker, _nbClient, _nbMessagePerClient));
	}

}
