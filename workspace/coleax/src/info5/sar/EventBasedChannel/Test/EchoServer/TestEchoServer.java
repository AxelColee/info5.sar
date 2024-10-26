package info5.sar.EventBasedChannel.Test.EchoServer;

import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Broker;
import info5.sar.EventBasedChannel.Impl.BrokerManager;
import info5.sar.EventBasedChannel.Impl.EventPump;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedChannel.Test.EchoServer.Client.EchoClient;
import info5.sar.EventBasedChannel.Test.EchoServer.Server.EchoServer;

public class TestEchoServer {
	private Task _clients;
	private Task _server;
	
	private IBroker _serverBroker;
	private IBroker _clientBroker;
	
	public TestEchoServer() {
		BrokerManager.getInstance().clean();
		_serverBroker = new Broker("serverBroker");
		_clientBroker = new Broker("clientBroker");
	}
	
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
	
	public void allTests() {

		int[] clientCounts = {1, 3, 10, 20, 100};
		int[] messageCounts = {1, 3, 10, 20, 100};

		for (int nbMessages : messageCounts) {
			for (int nbClient : clientCounts) {
				test(nbClient, nbMessages);
			}
		}
		
		System.out.println("ECHO SERVER TESTS PASSED");	
	}
	
	public static void main(String[] args) {
		
		TestEchoServer testEchoServer = new TestEchoServer();

		testEchoServer.allTests();
	}
	
	

}
