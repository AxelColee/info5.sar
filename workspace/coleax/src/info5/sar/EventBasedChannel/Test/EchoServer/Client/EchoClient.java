package info5.sar.EventBasedChannel.Test.EchoServer.Client;

import info5.sar.EventBasedChannel.Abstract.IBroker;

/**
 * The {@code EchoClient} class implements the Runnable interface and represents a client
 * that connects to a broker and sends a specified number of messages.
 * 
 * <p>This class is used to create an instance of a client that will connect to a 
 * server broker and send messages. The connection details and the number of messages 
 * to be sent are provided during the instantiation of the EchoClient object.</p>
 * 
 * <p>Fields:</p>
 * <ul>
 *  <li>{@code _broker} - The broker to which the client will connect.</li>
 * <li>{@code _nbMessagePerClient} - The number of messages to be sent by the client.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #run()} - Connects to the broker and sends the specified number of messages.</li>
 * </ul>
 */
public class EchoClient implements Runnable{

	private IBroker _broker;
	private int _nbMessagePerClient;
	
	public EchoClient(IBroker broker, int nbMessagePerClient) {
		_broker = broker;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void run() {
		_broker.connect("serverBroker", 80, new EchoClientConnectLIstener(_nbMessagePerClient));		
	}

}
