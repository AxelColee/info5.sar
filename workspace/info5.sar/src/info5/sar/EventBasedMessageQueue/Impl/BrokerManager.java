package info5.sar.EventBasedMessageQueue.Impl;

import java.util.HashMap;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.AcceptListener;

public class BrokerManager {
	private static final BrokerManager INSTANCE = new BrokerManager();
	
	private HashMap<String, QueueBroker> _brokers;
	
	private BrokerManager() {
		_brokers = new HashMap<String, QueueBroker>();
	}
	
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	public QueueBroker getBroker(String name) {
		QueueBroker broker =  _brokers.get(name);
		
		return broker;
	}
	
	public void registerBroker(QueueBroker broker) {
		
		String name = broker.name();
		
		if(_brokers.containsKey(name)) {
			throw new IllegalStateException("Two Broker have the same name");
		}
		
		this._brokers.put(name, broker);
	}

}
