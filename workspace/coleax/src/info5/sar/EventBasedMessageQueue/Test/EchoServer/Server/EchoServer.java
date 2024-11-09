package info5.sar.EventBasedMessageQueue.Test.EchoServer.Server;

import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

/**
 * {@code EchoServer} is an event-based server that listens for client connections
 * and echoes back any messages received from clients. It utilizes an {@link IQueueBroker}
 * to handle communication between clients and the server.
 * 
 * <p>This class implements the {@link Runnable} interface, allowing the server to be started 
 * and managed within an event-driven context rather than using threads.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _broker} - The {@link IQueueBroker} instance used to manage communication between clients and the server.</li>
 *   <li>{@code _nbClient} - The maximum number of clients the server can handle simultaneously.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoServer(IQueueBroker, int)}: Creates an instance of the server with the specified broker and client limit.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #run()}: Binds the server to a specified port and listens for client connections, initiating message echoing upon connection.</li>
 * </ul>
 * 
 * @see IQueueBroker
 * @see EchoServerAcceptListener
 */
public class EchoServer implements Runnable{
	
	private IQueueBroker _broker;
	private final int _nbClient;
	
	public EchoServer(IQueueBroker broker, int nbClient) {
		_broker = broker;
		_nbClient = nbClient;
	}


	/**
	 * Binds the server to a port and listens for client connections.
	 */
	@Override
	public void run() {
		_broker.bind(80, new EchoServerAcceptListener(_broker, _nbClient));
	}

}
