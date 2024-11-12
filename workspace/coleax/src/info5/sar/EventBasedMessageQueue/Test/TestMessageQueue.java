package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedMessageQueue.Test.EchoServer.TestEchoServer;

/**
 * {@code TestMessageQueue} is a testing class that aggregates and runs all tests related to
 * the message queue system, including echo server and client functionality.
 * 
 * <p>This class serves as a single entry point for executing all message queue-related tests,
 * ensuring that the system components are functioning as expected. It leverages the
 * {@link TestEchoServer} class to conduct echo server-specific tests, covering different
 * configurations and loads.</p>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #allMessageQueueTests()}: Runs all message queue tests and prints a success message if all pass.</li>
 * </ul>
 * 
 * @see TestEchoServer
 */

public class TestMessageQueue {

	/**
	 * Runs all message queue tests, including echo server and client tests.
	 * Prints a success message if all tests pass.
	 */
	public void allMessageQueueTests() {
		
		new TestEchoServer().allTests();
		
		System.out.println("ALL MESSAGE QUEUE TESTS PASSED");
	}
	
	/**
	 * Main method for running all message queue tests.
	 * 
	 * @param args Command-line arguments(unused).
	 */
	public static void main(String[] args) {
		new TestMessageQueue().allMessageQueueTests();
	}
}