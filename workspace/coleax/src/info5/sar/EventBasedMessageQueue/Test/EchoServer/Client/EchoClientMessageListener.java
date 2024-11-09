package info5.sar.EventBasedMessageQueue.Test.EchoServer.Client;

import java.util.List;

import info5.sar.EventBasedMessageQueue.Abstract.IMessageQueue;
import info5.sar.EventBasedMessageQueue.Abstract.MessageListener;

/**
 * {@code EchoClientMessageListener} is a subclass of {@link MessageListener} that listens for messages
 * from an {@link IMessageQueue} and verifies that the received messages match the expected messages.
 * 
 * <p>This listener is used to validate the integrity of messages in an echo server setup, where messages
 * sent to the server are expected to be echoed back. It maintains a list of expected messages and compares
 * each received message to ensure they match, asserting any discrepancies.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _messages} - A list of byte arrays representing the expected messages to be received in sequence.</li>
 *   <li>{@code _counter} - A counter tracking the current message being verified.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #EchoClientMessageListener(IMessageQueue, List)}: Initializes the listener with a message queue and a list of expected messages.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #received(byte[])}: Called when a message is received from the queue. Verifies that it matches the expected message.</li>
 *   <li>{@link #closed()}: Called when the connection is closed. Checks that the queue is properly closed.</li>
 *   <li>{@link #sent(byte[])}: No action is required in this method for echo validation.</li>
 * </ul>
 * 
 * @see MessageListener
 * @see IMessageQueue
 */
public class EchoClientMessageListener extends MessageListener {
	
	private List<byte[]> _messages;
	private int _counter;
	
	public EchoClientMessageListener(IMessageQueue queue, List<byte[]> messages) {
		super(queue);
		_messages = messages;
		_counter = 0;
	}


	/**
	 * Called when a message is received from the message queue.
	 * Verifies that the received message matches the expected message.
	 * @param bytes The received message.
	 */
	@Override
	public void received(byte[] bytes) {

		byte[] byteArrayReceived = _messages.get(_counter);
		
		for(int i = 0; i < byteArrayReceived.length; i++){
			assert(bytes[i] == byteArrayReceived[i]) : "Data recieved different from the one sent : " + i;
		}	
		
		_queue.close();
		
		_counter++;

		
	}

	/**
	 * Called when the message queue is closed.
	 * Asserts that the message queue is indeed closed and prints a message indicating the client passed.
	 */
	@Override
	public void closed() {
		
		assert(_queue != null) : "Client Queue not initialized";
		assert(_queue.closed() == true) : "Client Queue not disconnected";
		
		System.out.println("Client passed");
	}

	/**
	 * Called when a message is sent to the message queue.
	 * Does nothing in this implementation.
	 * @param message The message sent.
	 */
	@Override
	public void sent(byte[] message) {
		//Nothing to do here
	}

}
