package info5.sar.EventBasedMessageQueue.Test;

import info5.sar.EventBasedMessageQueue.Abstract.Task;
import info5.sar.EventBasedMessageQueue.Test.Client.EchoClient;
import info5.sar.EventBasedMessageQueue.Test.Server.EchoServer;

public class TestMain {
	
	private Task _client1;
	private Task _client2;
	private Task _client3;
	private Task _serveur;
	
	private EchoClient _clientRunnable;
	
	private EchoServer _serverRunnable;
	
	
	private void setup() {

//		QueueBroker serverBroker = new QueueBroker("serverBroker");
//	    QueueBroker clientBroker = new QueueBroker("clientBroker");
		
//		_clientRunnable = new EchoClient(clientBroker);
//		_serverRunnable = new EchoServer(serverBroker);
		
//	    _server = new Task();
//	    _client1 = new Task();
//	    _client2 = new EchoClient();
//	    _client3 = new EchoClient();
	}
	
	public static void main(String[] args) {
		
		TestMain test = new TestMain();
		
		EventPump.getInstance().start();
		
		test.setup();
		
		test._client1.post(test._clientRunnable);
		test._client2.post(test._clientRunnable);
		test._client3.post(test._clientRunnable);
		
		test._serveur.post(test._serverRunnable);
			
				
	}

}
