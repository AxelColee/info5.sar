package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedChannel.Impl.EventPump;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Test.Client.EchoClient;
import info5.sar.EventBasedMessageQueue.Test.Server.EchoServer;

public class TestMain {

	private Task _clients;
	private Task _server;
	
	private IQueueBroker _serverBroker = new QueueBroker("serverBroker");
	private IQueueBroker _clientBroker = new QueueBroker("clientBroker");
	
	private void test(int nbClient, int nbMessagePerClient) {
		
		 _clients = new Task();
		 _server = new Task();
		
		Runnable clientRunnable = new EchoClient(_clientBroker, nbMessagePerClient);
		Runnable serverRunnable = new EchoServer(_serverBroker, nbClient);

		for(int i = 0; i < nbClient; i++) {
			_clients.post(clientRunnable);
		}
		
		_server.post(serverRunnable);
		
		EventPump.getInstance().run();
	}
	
	public static void main(String[] args) {
		
		TestMain test = new TestMain();
		
		int nbMessages = 1;
		test.test(1, nbMessages);
		test.test(3, nbMessages);
		test.test(10, nbMessages);
		test.test(20, nbMessages);
		test.test(100, nbMessages);
		
		nbMessages = 3;
		test.test(1, nbMessages);
		test.test(3, nbMessages);
		test.test(10, nbMessages);
		test.test(20, nbMessages);
		test.test(100, nbMessages);
		
		nbMessages = 10;
		test.test(1, nbMessages);
		test.test(3, nbMessages);
		test.test(10, nbMessages);
		test.test(20, nbMessages);
		test.test(100, nbMessages);
		
		nbMessages = 20;
		test.test(1, nbMessages);
		test.test(3, nbMessages);
		test.test(10, nbMessages);
		test.test(20, nbMessages);
		test.test(100, nbMessages);
		
		nbMessages = 100;
		test.test(1, nbMessages);
		test.test(3, nbMessages);
		test.test(10, nbMessages);
		test.test(20, nbMessages);
		test.test(100, nbMessages);
		
		System.out.println("TEST PASSED");	
				
	}

}

