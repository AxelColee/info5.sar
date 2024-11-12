package info5.sar;

import info5.sar.EventBasedChannel.Test.TestChannel;
import info5.sar.EventBasedMessageQueue.Test.TestMessageQueue;


/**
 * {@code TestMain} serves as the main entry point for running all tests within the
 * `info5.sar` package, including channel and message queue tests.
 * 
 * <p>It aggregates tests from various components and ensures that both channel-related
 * and message queue-related functionalities are verified. It outputs a success message upon
 * passing all tests, validating the integrity of the entire system.</p>
 * 
 * <p><strong>Usage:</strong> Execute this class to run all tests and confirm that all system
 * components function as expected in an event-driven architecture.</p>
 * 
 * <p><strong>Related Tests:</strong></p>
 * <ul>
 *   <li>{@link TestChannel}: Runs tests for event-based channels.</li>
 *   <li>{@link TestMessageQueue}: Runs tests for event-based message queues, including echo server functionality.</li>
 * </ul>
 */
public class TestMain {
	
	public static void main(String[] args) {
		
		new TestChannel().allChannelTests();
		new TestMessageQueue().allMessageQueueTests();
		
		System.out.println("ALL TESTS PASSED");
		
	}

}
