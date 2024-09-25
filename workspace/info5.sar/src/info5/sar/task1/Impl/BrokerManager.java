package info5.sar.task1.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;

public final class BrokerManager {
	
	private static final BrokerManager INSTANCE = new BrokerManager();
	
	private List<Broker> _brokers; // Better to use a HAshMap
	
	private BrokerManager() {
		_brokers = new LinkedList<Broker>();
	}
	
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	
	//Avoir une methode de get plutot que connect et ensuite faire le connect dans la classe appelante
	public synchronized Channel connect(BrokerImpl brokerConnect, String name, int port) {
		for(Broker broker: _brokers) {
			BrokerImpl brokerImpl = (BrokerImpl) broker;
			if(brokerImpl.getName().equals(name)) {
				return brokerImpl.connect(brokerConnect, port);
			}
		}
		return null;
	}
	
	public synchronized void addBroker(Broker broker) {
		//check if this broker is already in the list or if one has the same name 
		this._brokers.add(broker);
	}

}
