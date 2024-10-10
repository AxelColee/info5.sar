package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedMessageQueue.Impl.*;
import info5.sar.EventBasedMessageQueue.Impl.EventPump;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Test.Client.EchoClient;
import info5.sar.EventBasedMessageQueue.Test.Server.EchoServer;

public class TestMain {
	
	private Task _client1;
	private Task _client2;
	private Task _client3;
	private Task _server;
	
	private EchoClient _clientRunnable;
	
	private EchoServer _serverRunnable;
	
	
	private void setup() {

		QueueBroker serverBroker = new QueueBroker("serverBroker");
	    QueueBroker clientBroker = new QueueBroker("clientBroker");
		
		_clientRunnable = new EchoClient(clientBroker);
		_serverRunnable = new EchoServer(serverBroker);
		
	    _server = new Task();
	    _client1 = new Task();
	    _client2 = new Task();
	    _client3 = new Task();
		
		EventPump.getInstance().start();
		
	}
	
	public static void main(String[] args) {
		
		TestMain test = new TestMain();
		

		
		test.setup();
		
		test._client1.post(test._clientRunnable);
		test._client2.post(test._clientRunnable);
		test._client3.post(test._clientRunnable);
		
		test._server.post(test._serverRunnable);
		
		try {
			EventPump.getInstance().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("TEST PASSED");
			
				
	}

}
