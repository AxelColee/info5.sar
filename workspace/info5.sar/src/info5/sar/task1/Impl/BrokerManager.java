package info5.sar.task1.Impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;

public final class BrokerManager {
	
	private static final BrokerManager INSTANCE = new BrokerManager();
	
	private HashMap<String, Broker> _brokers;
	
	private BrokerManager() {
		_brokers = new HashMap<String, Broker>();
	}
	
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	
	public Channel connect(Broker brokerConnect, String name, int port) {
		BrokerImpl broker = (BrokerImpl) _brokers.get(name);
		
		if(broker == null) {
			return null;
		}
		
		return broker.connect(brokerConnect, port);
		
	}
	
	public synchronized Broker getBroker(String name) {
		BrokerImpl broker = (BrokerImpl) _brokers.get(name);
		
		return broker;
	}
	
	public synchronized void addBroker(Broker broker) {
		
		BrokerImpl brokerImpl = (BrokerImpl) broker;
		String name = brokerImpl.getName();
		
		if(_brokers.containsKey(name)) {
			throw new IllegalStateException("Two Broker have the same name");
		}
		
		this._brokers.put(name, broker);
	}

}
