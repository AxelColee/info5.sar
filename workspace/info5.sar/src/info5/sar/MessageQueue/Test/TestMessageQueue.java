package info5.sar.MessageQueue.Test;

import info5.sar.MessageQueue.Abstract.QueueBroker;

public class TestMessageQueue {
	
	private EchoClient client1;
	private EchoClient client2;
	private EchoClient client3;
	
	private EchoServer server;
	
	private void setup() {
		
		//Broker brokerClient = new Broker("toto");
		//Broker brokerServer = new Broker("titi");
		
		//QueueBroker queueBrokerClient = new QueueBroker(brokerClient);
		//QueueBroker queueBrokerServer = new QueueBroker(brokerServer);
		
		//this.client1 = new EchoClient(queueBrokerClient);
		//this.client2 = new EchoClient(queueBrokerClient);
		//this.client3 = new EchoClient(queueBrokerClient);
		
		//this.server = new EchoClient(queueBrokerServer);		
	}
	
	public static void main(String[] args) {
		
		TestMessageQueue test = new TestMessageQueue();
		
		test.setup();
		
		test.client1.start();
		test.client2.start();
		test.client3.start();
		
		test.server.start();
		
		try{
			test.server.join();
			
			test.client1.join();
			test.client2.join();
			test.client3.join();
	
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.println("TEST PASSED");
		
		
	}

}
