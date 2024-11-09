package info5.sar.EventBasedMessageQueue.Test.EchoServer.Client;

import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;


/**
 * {@code EchoClient} is a client class that connects to a message queue broker and
 * sends a specified number of messages in an event-driven architecture.
 * 
 * <p>This class operates in a event-based environment, relying on an
 * {@link IQueueBroker} to establish connections with a server broker and handle
 * message transmission. The connection events are managed through an {@link IConnectListener},
 * which responds to connection establishment or failure.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _broker} - The {@link IQueueBroker} instance used to connect to the server broker.</li>
 *   <li>{@code _nbMessage} - The number of messages to send once connected.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoClient(IQueueBroker, int)}: Initializes the client with a broker and message count.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #run()}: Initiates a connection to the server broker and starts message transmission upon connection.</li>
 * </ul>
 * 
 * @see IQueueBroker
 * @see IConnectListener
 */
public class EchoClient implements Runnable {
	
	private IQueueBroker _broker;
	private int _nbMessage;
	
	public EchoClient(IQueueBroker broker, int nbMessage) {
		_broker = broker;
		_nbMessage = nbMessage;
	}

	/**
	 * Initiates a connection to the server broker and starts message transmission upon connection.
	 */
	@Override
	public void run() {
				
		IConnectListener connectListener = new EchoClientConnectListener(_nbMessage);
		
		_broker.connect("serverBroker", 80, connectListener);	
	}

}
