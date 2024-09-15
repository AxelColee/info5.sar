package info5.sar.task1.Test;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;
import info5.sar.task1.Task;

public class EchoServer {

	public static int ARRAY_LENGTH = 256;
	
	public Task client;
	public Task server;

	private byte[] dataToSend = new byte[ARRAY_LENGTH];
	private byte[] dataReceived = new byte[ARRAY_LENGTH];

	private Channel clientChannel;
	private Channel serverChannel;

	public Runnable getClientRunnable() {
        return () -> {

			Broker broker = client.getBroker();

			clientChannel = broker.connect("toto", 80);

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

			//Sending all the data
			for(int totalReadBytes = 0; totalReadBytes < ARRAY_LENGTH;){
				int readBytes = clientChannel.read(dataReceived, 0, ARRAY_LENGTH);
				if (readBytes < 0) {
					throw new Error("Read error");
				}
				totalReadBytes += readBytes;
			}

			clientChannel.disconnect();

		};
	}

	public Runnable getServerRunnable() {
        return () -> {

    		byte[] dataReceived = new byte[ARRAY_LENGTH];

			Broker broker = server.getBroker();

			serverChannel = broker.accept(80);

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
		};
	}

	private void setup(){
		//this.client = new ...
		//this.server = new ...
	}

	public static void main(){

		EchoServer echoServer = new EchoServer();

		echoServer.setup();

		echoServer.server.start();
		echoServer.client.start();

		try{
			echoServer.server.join();
			echoServer.client.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		//Tests
		for(int i = 0; i < ARRAY_LENGTH; i++){
			assert(echoServer.dataToSend[i] == echoServer.dataReceived[i]) : "Data recieved different from the one sent : " + i;
		}	

		assert(echoServer.clientChannel != null) : "Client Channel not initialized";
		assert(echoServer.clientChannel.disconnected() == true) : "Client Channel not disconnected";

		assert(echoServer.serverChannel != null) : "Client Channel not initialized";
		assert(echoServer.serverChannel.disconnected() == true): "Client Channel not disconnected";
		
		System.out.println("TEST PASSED");

	}
}
