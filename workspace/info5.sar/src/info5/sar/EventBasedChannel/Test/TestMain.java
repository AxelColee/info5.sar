package info5.sar.EventBasedChannel.Test;

import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Abstract.ITask;
import info5.sar.EventBasedChannel.Test.Client.EchoClient;
import info5.sar.EventBasedChannel.Test.Server.EchoServer;

public class TestMain {
	private ITask _client1;
	private ITask _client2;
	private ITask _client3;
	private ITask _server;
	
	private EchoClient _clientRunnable;
	
	private EchoServer _serverRunnable;
	
	
	private void setup() {

//		IBroker serverBroker = new Broker("serverBroker");
//	    IBroker clientBroker = new Broker("clientBroker");
//		
//		_clientRunnable = new EchoClient(clientBroker);
//		_serverRunnable = new EchoServer(serverBroker);
//		
//	    _server = new Task();
//	    _client1 = new Task();
//	    _client2 = new Task();
//	    _client3 = new Task();
//		
//		EventPump.getInstance().start();
		
	}
	
	public static void main(String[] args) {
		
		TestMain test = new TestMain();
		

		
		test.setup();
		
		test._client1.post(test._clientRunnable);
		test._client2.post(test._clientRunnable);
		test._client3.post(test._clientRunnable);
		
		test._server.post(test._serverRunnable);
		
//		try {
//			EventPump.getInstance().join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("TEST PASSED");
			
				
	}

}