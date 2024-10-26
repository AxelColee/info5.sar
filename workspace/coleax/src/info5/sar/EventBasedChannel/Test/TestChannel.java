package info5.sar.EventBasedChannel.Test;

import info5.sar.EventBasedChannel.Test.EchoServer.TestEchoServer;

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