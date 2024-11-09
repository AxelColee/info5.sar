package info5.sar.EventBasedChannel.Impl;

import java.util.HashMap;

/**
 * The {@code BrokerManager} class is a singleton that manages a collection of Broker instances.
 * It provides methods to register, retrieve, and clean brokers.
 * 
 * <p>This class ensures that only one instance of BrokerManager exists and provides
 * a global point of access to it.</p>
 * 
 * <p><strong>Methods:</strong></p>
 * <ul>
 *  <li>{@link #getInstance()} - Returns the singleton instance of BrokerManager.</li>
 * <li>{@link #getBroker(String)} - Returns the broker with the specified name.</li>
 * <li>{@link #registerBroker(Broker)} - Registers a broker with the manager.</li>
 * <li>{@link #clean()} - Removes all brokers from the manager.</li>
 * </ul>	
 * 
 * <p><strong>Fields:</strong></p>
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
	
	/**
	 * Returns the singleton instance of BrokerManager.
	 * @return the singleton instance of BrokerManager
	 */
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	/**
	 * Returns the broker with the specified name.
	 * @param name the name of the broker
	 * @return the broker with the specified name
	 * @throws IllegalStateException if two brokers have the same name
	 * @see Broker
	 */
	public Broker getBroker(String name) {
		Broker broker =  _brokers.get(name);
		
		return broker;
	}
	
	/**
	 * Registers a broker with the manager.
	 * @param broker the broker to register
	 * @throws IllegalStateException if two brokers have the same name
	 * @see Broker
	 */
	public void registerBroker(Broker broker) {
		
		String name = broker.name();
		
		if(_brokers.containsKey(name)) {
			throw new IllegalStateException("Two Broker have the same name");
		}
		
		this._brokers.put(name, broker);
	}
	
	/**
	 * Removes all brokers from the manager.
	 */
	public void clean() {
		_brokers = new HashMap<String, Broker>();
	}

}