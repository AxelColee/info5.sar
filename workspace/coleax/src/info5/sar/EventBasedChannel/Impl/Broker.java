package info5.sar.EventBasedChannel.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;

/**
 * The {@code Broker} class implements the IBroker interface and represents a broker
 * that manages connections and bindings for different ports.
 * 
 * <p>This class is responsible for registering itself with the BrokerManager,
 * binding and unbinding ports with listeners, and handling connection requests
 * to other brokers.</p>
 * 
 * <p>It maintains a map of port numbers to IAcceptListener instances, which
 * are used to handle incoming connections on specific ports.</p>
 * 
 * <p>When a connection request is made, the broker checks if the requested
 * port is bound and, if so, establishes a communication channel between the
 * connecting party and the bound listener.</p>
 * 
 * <p>If the port is not bound, the connection request is retried a limited
 * number of times before being refused.</p>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #Broker(String)} - Constructs a Broker with the specified name and registers it with the BrokerManager.</li>
 *   <li>{@link #unbind(int)} - Unbinds the listener from the specified port.</li>
 *   <li>{@link #bind(int, IAcceptListener)} - Binds a listener to the specified port.</li>
 *   <li>{@link #connect(String, int, IConnectListener)} - Attempts to connect to another broker on the specified port.</li>
 *   <li>{@link #name()} - Returns the name of the broker.</li>
 * </ul>
 * 
 * <p>Private Methods:</p>
 * <ul>
 *   <li>{@link #_connect(int, IConnectListener)} - Handles the connection logic for the specified port.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code _brokerManager} - The BrokerManager instance managing this broker.</li>
 *   <li>{@code _name} - The name of the broker.</li>
 *   <li>{@code _binds} - A map of port numbers to IAcceptListener instances.</li>
 * </ul>
 */

public class Broker implements IBroker{
	
	private BrokerManager _brokerManager;
	private String _name;
	private Map<Integer,IAcceptListener> _binds;

	public Broker(String name) {
		_name = name;
		_brokerManager = BrokerManager.getInstance();
		_brokerManager.registerBroker(this);
		_binds = new HashMap<Integer, IAcceptListener>();
	}

	@Override
	public boolean unbind(int port) {
		
		if(!_binds.containsKey(port)) {
			return false;
		}
		
		_binds.remove(port);
		return true;
	}

	@Override
	public boolean bind(int port, IAcceptListener listener) {
		_binds.put(port, listener); //Change the linked listener if port already binded
		return true;
	}

	@Override
	public boolean connect(String name, int port, IConnectListener listener) {
		Broker broker = _brokerManager.getBroker(name);
		if(broker == null) {
			listener.refused();
			return false;
		}else {
			broker._connect(port, listener);
			return true;
		}
		
	}
	
	//Private method to handle the connection logic (called by connect)
	private void _connect(int port, IConnectListener listener) {
		if(_binds.containsKey(port)) {
			
			Channel channelAccept = new Channel();
			Channel channelConnect = new Channel();
			
			channelAccept._out = channelConnect._in;
			channelConnect._out = channelAccept._in;
			
			channelAccept._rch = channelConnect;
			channelConnect._rch = channelAccept;
			
			listener.connected(channelConnect);
			_binds.get(port).accepted(channelAccept);
							
		}else {
			Task from = Task.task();
			int remainingTries = from.getRemainingTries() - 1;
			if(remainingTries == 0) {
				listener.refused();
				return;
			}
			
			Task t = new Task(remainingTries);
			t.post(() -> _connect(port, listener));
		}
		
	}
	
	@Override
	public String name() {
		return _name;
	}
}
