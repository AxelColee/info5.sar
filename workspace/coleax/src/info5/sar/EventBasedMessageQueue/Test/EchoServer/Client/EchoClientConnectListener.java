package info5.sar.EventBasedMessageQueue.Test.EchoServer.Client;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;


/**
 * {@code EchoClientConnectListener} is an implementation of the {@link IConnectListener} interface.
 * It manages the connection to a message queue and handles the sending of a predefined number of messages
 * upon successful connection.
 * 
 * <p>This listener generates a specified number of unique messages upon connection and sends each
 * to the message queue. If the connection is refused, an error message is printed to the standard error stream.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _nbMessage} - The number of messages to be generated and sent upon connection.</li>
 *   <li>{@code _messages} - A list storing the generated messages as byte arrays.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoClientConnectListener(int)}: Initializes the listener with a specified number of messages to send.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #connected(IMessageQueue)}: Called when the connection to the message queue is successfully established. Generates and sends messages to the queue.</li>
 *   <li>{@link #refused()}: Called when the connection to the message queue is refused. Logs an error message.</li>
 * </ul>
 * 
 * @see IConnectListener
 * @see IMessageQueue
 */
public class EchoClientConnectListener implements IConnectListener {
	
	private int _nbMessage;
	private List<byte[]> _messages;

	public EchoClientConnectListener(int nbMessage) {
		_nbMessage = nbMessage;
		_messages = new LinkedList<byte[]>();
	}

	/**
	 * Called when the connection to the message queue is successfully established.
	 * Generates a specified number of unique messages and sends each to the message queue.
	 * @param queue The message queue to which messages are sent.
	 */
	@Override
	public void connected(IMessageQueue queue) {
		
		for(int i = 0; i < _nbMessage; i++) {
			_messages.add(UUID.randomUUID().toString().repeat(10).getBytes());
		}
		
		queue.setListener(new EchoClientMessageListener(queue, _messages));
		
		for(int i = 0; i < _nbMessage; i++) {
			final int index = i;
			queue.send(_messages.get(index));
		}
		

	}

	/**
	 * Called when the connection to the message queue is refused.
	 * Prints an error message to the standard error stream.
	 */
	@Override
	public void refused() {
		System.err.println("Connection refused");
	}

}
