package info5.sar.EventBasedChannel.Impl;

import java.util.HashMap;

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

}