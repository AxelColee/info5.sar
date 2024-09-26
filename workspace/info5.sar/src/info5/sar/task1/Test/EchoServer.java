package info5.sar.task1.Test;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;
import info5.sar.task1.Task;
import info5.sar.task1.Impl.BrokerImpl;
import info5.sar.task1.Impl.BrokerManager;
import info5.sar.task1.Impl.DisconnectedException;
import info5.sar.task1.Impl.TaskImpl;

public class EchoServer {

	public static int ARRAY_LENGTH = 256;
	
	public TaskImpl client1;
	public TaskImpl client2;
	public TaskImpl client3;
	public TaskImpl server;

	public Runnable getClientRunnable() {
        return () -> {

			TaskImpl client = (TaskImpl) Thread.currentThread();

			Broker broker = client.getBroker();

			byte[] dataToSend = new byte[ARRAY_LENGTH];
	 		byte[] dataReceived = new byte[ARRAY_LENGTH];

			Channel clientChannel = broker.connect("toto", 80);
			
			for(int i = 0; i < ARRAY_LENGTH; i++)
				dataToSend[i] = (byte) i;

			//Sending all the data
			for(int totalWroteBytes = 0; totalWroteBytes < 256;){
				int wroteBytes = 0;
				try {
					wroteBytes = clientChannel.write(dataToSend, 0, ARRAY_LENGTH);
				} catch (DisconnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (wroteBytes < 0) {
					throw new Error("Write error");
				}
				totalWroteBytes += wroteBytes;
			}


			//Getting all the data
			for(int totalReadBytes = 0; totalReadBytes < ARRAY_LENGTH;){
				int readBytes = 0;
				try {
					readBytes = clientChannel.read(dataReceived, 0, ARRAY_LENGTH);
				} catch (DisconnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (readBytes < 0) {
					throw new Error("Read error");
				}
				totalReadBytes += readBytes;
			}

			clientChannel.disconnect();


			//Tests
			for(int i = 0; i < ARRAY_LENGTH; i++){
				assert(dataToSend[i] == dataReceived[i]) : "Data recieved different from the one sent : " + i;
			}	

			assert(clientChannel != null) : "Client Channel not initialized";
			assert(clientChannel.disconnected() == true) : "Client Channel not disconnected";
			
		};
	}

	public Runnable getServerRunnable() {
        return () -> {

    		byte[] dataReceived = new byte[ARRAY_LENGTH];

    		Thread t = Thread.currentThread();
    		
			TaskImpl server = (TaskImpl) t;

			Broker broker = server.getBroker();
			
			for(int j = 0; j < 3; j++) {
				Channel serverChannel = broker.accept(80);

				for(int i = 0; i < ARRAY_LENGTH;){
					int readBytes = 0;
					try {
						readBytes = serverChannel.read(dataReceived, 0, ARRAY_LENGTH);
					} catch (DisconnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (readBytes < 0) {
						throw new Error("Read error");
					}
					i += readBytes;
				}

				for(int i = 0; i < ARRAY_LENGTH;){
					int wroteBytes = 0;
					try {
						wroteBytes = serverChannel.write(dataReceived, 0, ARRAY_LENGTH);
					} catch (DisconnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (wroteBytes < 0) {
						throw new Error("Write error");
					}
					i += wroteBytes;
				}
				
				serverChannel.disconnect();


				assert(serverChannel != null) : "Server Channel not initialized";
				assert(serverChannel.disconnected() == true): "Server Channel not disconnected";
				
			}
			
		};
	}

	private void setup(){
		
		
		Broker broker1 = new BrokerImpl("toto");
		Broker broker2 = new BrokerImpl("titi");
	
		this.client1 = new TaskImpl(broker2, this.getClientRunnable());
		this.client2 = new TaskImpl(broker2, this.getClientRunnable());
		this.client3 = new TaskImpl(broker2, this.getClientRunnable());
		
		this.server = new TaskImpl(broker1, this.getServerRunnable());
	}

	public static void main(String[] args){

		EchoServer echoServer = new EchoServer();

		echoServer.setup();

		echoServer.server.start();
		echoServer.client1.start();
		echoServer.client2.start();
		echoServer.client3.start();

		try{
			echoServer.server.join();
			echoServer.client1.join();
			echoServer.client2.join();
			echoServer.client3.join();

		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.println("TEST PASSED");

	}
}
