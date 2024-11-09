package info5.sar.EventBasedChannel.Test;

import info5.sar.EventBasedChannel.Test.EchoServer.TestEchoServer;

/**
 * The {@code TestChannel} class is responsible for running all channel tests.
 * It creates an instance of TestEchoServer and invokes its allTests method.
 * After running all tests, it prints a confirmation message to the console.
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 * <li>{@link #allChannelTests()}: Runs all channel tests.</li>
 * </ul>
 * 
 * @see TestEchoServer
 * 
 */
public class TestChannel {

	public void allChannelTests() {
		TestEchoServer testEchoServer = new TestEchoServer();
		
		testEchoServer.allTests();
		
		System.out.println("ALL CHANNEL TESTS PASSED");
	}
	
	public static void main(String[] args) {
		new TestChannel().allChannelTests();
	}
	
	

}