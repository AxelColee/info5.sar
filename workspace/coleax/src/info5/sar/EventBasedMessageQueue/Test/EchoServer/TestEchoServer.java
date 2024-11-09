package info5.sar.EventBasedMessageQueue.Test.EchoServer;

import info5.sar.EventBasedChannel.Impl.BrokerManager;
import info5.sar.EventBasedChannel.Impl.EventPump;
import info5.sar.EventBasedChannel.Impl.Task;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;
import info5.sar.EventBasedMessageQueue.Test.EchoServer.Client.EchoClient;
import info5.sar.EventBasedMessageQueue.Test.EchoServer.Server.EchoServer;

/**
 * {@code TestEchoServer} is responsible for testing the functionality of the {@link EchoServer} and
 * {@link EchoClient} by simulating multiple clients sending messages to an echo server.
 * 
 * <p>This class initializes the necessary brokers and tasks, sets up clients and a server, and runs
 * a series of tests with varying numbers of clients and messages, verifying the robustness and performance
 * of the echo server in an event-driven environment.</p>
 * 
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code _clients} - A task managing all client activities in the test.</li>
 *   <li>{@code _server} - A task managing the server activity in the test.</li>
 *   <li>{@code _serverBroker} - The broker used by the server to manage client connections.</li>
 *   <li>{@code _clientBroker} - The broker used by clients to communicate with the server.</li>
 * </ul>
 * 
 * <p><strong>Constructor:</strong></p>
 * <ul>
 *   <li>{@link #TestEchoServer()}: Initializes the test environment by creating brokers and clearing any previous broker configurations.</li>
 * </ul>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@link #test(int, int)}: Runs a test for a specified number of clients, each sending a specified number of messages.</li>
 *   <li>{@link #allTests()}: Runs a series of tests with varying numbers of clients and messages to ensure full coverage of the server’s handling capabilities.</li>
 * </ul>
 * 
 * @see EchoServer
 * @see EchoClient
 * @see EventPump
 * @see BrokerManager
 */
public class TestEchoServer {

	private Task _clients;
	private Task _server;
	
	private IQueueBroker _serverBroker;
	private IQueueBroker _clientBroker;
	
	public TestEchoServer() {
		BrokerManager.getInstance().clean();
		_serverBroker = new QueueBroker("serverBroker");
		_clientBroker = new QueueBroker("clientBroker");
	}
	
	/**
	 * Runs a test with a specified number of clients and messages.
	 * 
	 * @param nbClient The number of clients to simulate.
	 * @param nbMessagePerClient The number of messages to send per client.
	 */
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
	
	/**
	 * Runs all tests with varying numbers of clients and messages.
	 */
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

