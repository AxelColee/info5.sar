package info5.sar.task1.Test;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;
import info5.sar.task1.Task;

public class EchoServer {

	public static int ARRAY_LENGTH = 256;
	
	public Task client1;
	public Task client2;
	public Task client3;
	public Task server;

	public Runnable getClientRunnable() {
        return () -> {

			Task client = (Task) Thread.currentThread();

			Broker broker = client.getBroker();

			byte[] dataToSend = new byte[ARRAY_LENGTH];
	 		byte[] dataReceived = new byte[ARRAY_LENGTH];

			Channel clientChannel = broker.connect("toto", 80);

			for(int i = 0; i < ARRAY_LENGTH; i++)
				dataToSend[i] = (byte) i;

			//Sending all the data
			for(int totalWroteBytes = 0; totalWroteBytes < 256;){
				int wroteBytes = clientChannel.write(dataToSend, 0, ARRAY_LENGTH);
				if (wroteBytes < 0) {
					throw new Error("Write error");
				}
				totalWroteBytes += wroteBytes;
			}

			//Getting all the data
			for(int totalReadBytes = 0; totalReadBytes < ARRAY_LENGTH;){
				int readBytes = clientChannel.read(dataReceived, 0, ARRAY_LENGTH);
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

			Task server = (Task) Thread.currentThread();

			Broker broker = server.getBroker();

			Channel serverChannel = broker.accept(80);

			for(int i = 0; i < ARRAY_LENGTH;){
				int readBytes = serverChannel.read(dataReceived, 0, ARRAY_LENGTH);
				if (readBytes < 0) {
					throw new Error("Read error");
				}
				i += readBytes;
			}

			for(int i = 0; i < ARRAY_LENGTH;){
				int wroteBytes = serverChannel.write(dataReceived, 0, ARRAY_LENGTH);
				if (wroteBytes < 0) {
					throw new Error("Write error");
				}
				i += wroteBytes;
			}

			serverChannel.disconnect();

			assert(serverChannel != null) : "Server Channel not initialized";
			assert(serverChannel.disconnected() == true): "Server Channel not disconnected";
		};
	}

	private void setup(){
		//this.client1 = new ...
		//this.client2 = new ...
		//this.client3 = new ...

		//this.server = new ...
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
