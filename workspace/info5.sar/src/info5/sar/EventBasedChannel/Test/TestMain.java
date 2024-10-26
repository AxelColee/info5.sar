package info5.sar.EventBasedChannel.Test;

import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Broker;
import info5.sar.EventBasedChannel.Impl.EventPump;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedChannel.Test.Client.EchoClient;
import info5.sar.EventBasedChannel.Test.Server.EchoServer;

public class TestMain {
	
	private Task _clients;
	private Task _server;
	
	private IBroker _serverBroker = new Broker("serverBroker");
	private IBroker _clientBroker = new Broker("clientBroker");
	
	private void test(int nbClient, int nbMessagePerClient) {
		
		 _clients = new Task();
		 _server = new Task();
		
		Runnable clientRunnable = new EchoClient(_clientBroker, nbMessagePerClient);
		Runnable serverRunnable = new EchoServer(_serverBroker, nbClient, nbMessagePerClient);

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