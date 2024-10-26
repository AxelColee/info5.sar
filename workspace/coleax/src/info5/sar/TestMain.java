package info5.sar;

import info5.sar.EventBasedChannel.Test.TestChannel;
import info5.sar.EventBasedMessageQueue.Test.TestMessageQueue;

public class TestMain {
	
	public static void main(String[] args) {
		
		new TestChannel().allChannelTests();
		new TestMessageQueue().allMessageQueueTests();
		
		System.out.println("ALL TESTS PASSED");
		
	}

}
