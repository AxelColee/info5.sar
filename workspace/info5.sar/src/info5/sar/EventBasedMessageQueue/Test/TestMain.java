package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedMessageQueue.Test.Client.EchoClient;
import info5.sar.EventBasedMessageQueue.Test.Server.EchoServer;

public class TestMain {
	
	private EchoClient _client1;
	private EchoClient _client2;
	private EchoClient _client3;
	
	private EchoServer _server;
	
	private void setup() {
//		QueueBroker serverBroker = new QueueBroker("serverBroker");
//	    QueueBroker clientBroker = new QueueBroker("clientBroker");
//
//	    _server = new EchoServer(serverBroker);
//	    _client1 = new EchoClient(clientBroker1);
//	    _client2 = new EchoClient(clientBroker2);
//	    _client3 = new EchoClient(clientBroker3);
	}
	
	public static void main(String[] args) {
		
		TestMain test = new TestMain();
		
		test._client1.start();
		test._client2.start();
		test._client3.start();
		test._server.start();
		
		try {
			test._client1.join();
			test._client2.join();
			test._client3.join();
			test._server.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("TEST PASSED");
		
	}

}
