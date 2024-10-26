package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedMessageQueue.Test.EchoServer.TestEchoServer;

public class TestMessageQueue {

	public void allMessageQueueTests() {
		
		new TestEchoServer().allTests();
		
		System.out.println("ALL MESSAGE QUEUE TESTS PASSED");
	}
	
	public static void main(String[] args) {
		new TestMessageQueue().allMessageQueueTests();
	}
}