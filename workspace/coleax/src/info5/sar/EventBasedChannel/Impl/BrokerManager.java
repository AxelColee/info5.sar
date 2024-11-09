package info5.sar.EventBasedChannel.Impl;

import java.util.HashMap;

/**
 * The BrokerManager class is a singleton that manages a collection of Broker instances.
 * It provides methods to register, retrieve, and clean brokers.
 * 
 * <p>This class ensures that only one instance of BrokerManager exists and provides
 * a global point of access to it.</p>
 * 
 * <p>Methods:</p>
 * <ul>
 *  <li>{@link #getInstance()} - Returns the singleton instance of BrokerManager.</li>
 * <li>{@link #getBroker(String)} - Returns the broker with the specified name.</li>
 * <li>{@link #registerBroker(Broker)} - Registers a broker with the manager.</li>
 * <li>{@link #clean()} - Removes all brokers from the manager.</li>
 * </ul>	
 * 
 * <p>Fields:</p>
 * <ul>
 * <li>{@code _brokers} - A map of broker names to Broker instances.</li>
 * </ul>
 * 
 * <p>Note: This class is thread-safe.</p>
 * 
 * @see Broker
 */

public class BrokerManager {
	private static final BrokerManager INSTANCE = new BrokerManager();
	
	private HashMap<String, Broker> _brokers;
	
	private BrokerManager() {
		_brokers = new HashMap<String, Broker>();
	}
	
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	public Broker getBroker(String name) {
		Broker broker =  _brokers.get(name);
		
		return broker;
	}
	
	public void registerBroker(Broker broker) {
		
		String name = broker.name();
		
		if(_brokers.containsKey(name)) {
			throw new IllegalStateException("Two Broker have the same name");
		}
		
		this._brokers.put(name, broker);
	}
	
	public void clean() {
		_brokers = new HashMap<String, Broker>();
	}

}