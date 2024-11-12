package info5.sar.EventBasedChannel.Test.EchoServer;

import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Broker;
import info5.sar.EventBasedChannel.Impl.BrokerManager;
import info5.sar.EventBasedChannel.Impl.EventPump;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedChannel.Test.EchoServer.Client.EchoClient;
import info5.sar.EventBasedChannel.Test.EchoServer.Server.EchoServer;

/**
 * The {@code TestEchoServer} class is responsible for testing the EchoServer and EchoClient
 * functionality. It sets up the server and client brokers, creates tasks for clients
 * and server, and runs tests with varying numbers of clients and messages.
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 * <li>{@code _clients}: Task for managing client threads.</li>
 * <li>{@code _server}: Task for managing server thread.</li>
 * <li>{@code _serverBroker}: Broker for the server.</li>
 * <li>{@code _clientBroker}: Broker for the clients.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 * <li>{@link #test(int, int)}: Runs a test with a specified number of clients and messages.</li>
 * <li>{@link #allTests()}: Runs all tests with varying numbers of clients and messages.</li>
 * </ul>
 */
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
