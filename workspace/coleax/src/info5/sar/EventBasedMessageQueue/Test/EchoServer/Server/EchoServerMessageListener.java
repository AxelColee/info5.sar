package info5.sar.EventBasedMessageQueue.Test.EchoServer.Server;

import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;


/**
 * {@code EchoServerMessageListener} is a subclass of {@link MessageListener} that manages
 * message events for an echo server, handling the reception, sending, and closing of messages.
 * 
 * <p>This listener tracks the number of connected clients and ensures the server queue
 * is properly closed when all clients have disconnected. It operates in an event-driven
 * environment, responding to incoming messages and connection state changes.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _nbClient} - The total number of clients the server is expected to handle.</li>
 *   <li>{@code _counter} - The number of clients that have disconnected.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoServerMessageListener(IMessageQueue, int)}: Initializes the listener with the queue and client count.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #received(byte[])}: Called when a message is received, echoing it back to the sender.</li>
 *   <li>{@link #closed()}: Called when the connection is closed. Increments the client counter and logs success if all clients have disconnected.</li>
 *   <li>{@link #sent(byte[])}: Called after a message is sent to the queue, closing the queue.</li>
 * </ul>
 *
 * @see MessageListener
 * @see IMessageQueue
 */

public class EchoServerMessageListener extends MessageListener{
	
	private int _nbClient;
	private static int _counter = 0;

	public EchoServerMessageListener(IMessageQueue queue, int nbClient) {
		super(queue);
		_nbClient = nbClient;
	}

	/**
	 * Called when a message is received from the queue.
	 * Sends the message to the server queue.
	 * @param bytes The received message.
	 */
	@Override
	public void received(byte[] bytes) {
		_queue.send(bytes);
	}

	/**
	 * Called when the connection is closed.
	 * Increments the client counter and prints "Server Passed" if the number of clients has been reached.
	 */
	@Override
	public void closed() {
		
		_counter++;
		
		assert(_queue != null) : "Server queue not initialized";
		assert(_queue.closed() == true) : "Server queue not disconnected";
		
		if(_counter >= _nbClient) {
			_counter = 0;
			System.out.println("Server passed");
			
		}
	}

	/**
	 * Called when a message is sent to the queue.
	 * Closes the server queue.
	 * @param message The message sent to the queue.
	 */
	@Override
	public void sent(byte[] message) {
			_queue.close();
	}


}
