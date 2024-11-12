package info5.sar.EventBasedMessageQueue.Test.EchoServer.Server;

import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

/**
 * {@code EchoServerAcceptListener} is an implementation of the {@link IAcceptListener} interface
 * that listens for accepted connections to a message queue and manages client connections.
 * It assigns a message listener to each accepted queue and tracks the number of connected clients.
 * Once the maximum number of clients is reached, it unbinds the broker from port 80.
 * 
 * <p>This listener ensures that each connected client receives an {@link EchoServerMessageListener}
 * to handle message interactions and gracefully stops accepting new connections when the client limit is reached.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _broker} - The {@link IQueueBroker} instance used for managing the server’s client connections.</li>
 *   <li>{@code _nbClient} - The maximum number of clients the server is configured to handle.</li>
 *   <li>{@code _counter} - A counter tracking the current number of connected clients.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoServerAcceptListener(IQueueBroker, int)}: Initializes the listener with a specified broker and client limit.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #accepted(IMessageQueue)}: Called when a message queue connection is accepted, sets a listener for the queue, and unbinds the broker if the client limit is reached.</li>
 * </ul>

 * @see IAcceptListener
 * @see IMessageQueue
 * @see IQueueBroker
 * @see EchoServerMessageListener
 */
public class EchoServerAcceptListener implements IAcceptListener {

	private IQueueBroker _broker;
	private int _nbClient;
	private int _counter;
	
	 public EchoServerAcceptListener(IQueueBroker broker, int nbClient) {
		_broker = broker;
		_counter = 0;
		_nbClient = nbClient;
	}

	/**
	 * Called when a message is accepted in the queue.
	 * @param queue The message queue that accepted the message.
	 */
	@Override
	public void accepted(IMessageQueue queue) {
		
		_counter++;
		
		queue.setListener(new EchoServerMessageListener(queue, _nbClient));
		
		if(_counter >= _nbClient) {
			_broker.unbind(80);
		}
	}

}
