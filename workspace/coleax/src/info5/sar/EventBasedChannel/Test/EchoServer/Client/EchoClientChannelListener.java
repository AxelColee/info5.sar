package info5.sar.EventBasedChannel.Test.EchoServer.Client;

import java.util.List;

import info5.sar.EventBasedChannel.Abstract.IChannel;
import info5.sar.EventBasedChannel.Abstract.IChannelListener;

/**
 * EchoClientChannelListener is an implementation of the IChannelListener interface.
 * It listens to events on a channel and verifies that the data received matches the data sent.
 * It also handles disconnection events and ensures the channel is properly disconnected.
 * 
 * <p>Constructor:</p>
 * <ul>
 *   <li>{@link #EchoClientChannelListener(IChannel, List, int)}: Initializes the listener with the specified channel, list of byte arrays, and number of messages per client.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #read(byte[])}: Called when data is read from the channel. Verifies the received data and disconnects the channel if the expected number of messages have been received.</li>
 *   <li>{@link #disconnected()}: Called when the channel is disconnected. Asserts that the channel is indeed disconnected and prints a message indicating the client passed.</li>
 *   <li>{@link #wrote(byte[])}: Called when data is written to the channel. Reads the response from the channel.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code _bytes}: List of byte arrays to be compared with the received data.</li>
 *   <li>{@code _nbMessagePerClient}: Number of messages expected per client.</li>
 *   <li>{@code _channel}: The channel associated with this listener.</li>
 *   <li>{@code _counter}: Counter to keep track of the number of messages received.</li>
 * </ul>
 */
public class EchoClientChannelListener implements IChannelListener {
	
	private List<byte[]> _bytes;
	private int _nbMessagePerClient;
	private IChannel _channel;
	private int _counter =0;
	
	public EchoClientChannelListener(IChannel channel, List<byte[]> bytes, int nbMessagePerClient) {
		_channel = channel;
		_bytes = bytes;
		_nbMessagePerClient = nbMessagePerClient;
	}

	@Override
	public void read(byte[] bytes) {
		
		byte[] byteArrayReceived = _bytes.get(_counter);
		
		for(int i = 0; i < byteArrayReceived.length; i++){
			assert(bytes[i] == byteArrayReceived[i]) : "Data recieved different from the one sent : " + i;
		}	
		
		_counter++;

		if(_counter >= _nbMessagePerClient) {
			_channel.disconnect();
		}
		
	}

	@Override
	public void disconnected() {
		assert(_channel.disconnected() == true) : "Channel is not disconnected";
		
		System.out.println("Client Passed");

	}
	
	@Override
	public void wrote(byte[] bytes) {
		byte[] answer = new byte[bytes.length];
		_channel.read(answer);	
	}

}
