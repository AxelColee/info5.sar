package info5.sar.task1.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;

public final class BrokerManager {
	
	private static final BrokerManager INSTANCE = new BrokerManager();
	
	private List<Broker> _brokers;
	
	private BrokerManager() {
		_brokers = new LinkedList<Broker>();
	}
	
	public static BrokerManager getInstance() {
        return INSTANCE;
    }
	
	public Channel connect(BrokerImpl brokerConnect, String name, int port) {
		for(Broker broker: _brokers) {
			BrokerImpl brokerImpl = (BrokerImpl) broker;
			if(brokerImpl.getName().equals(name)) {
				return brokerImpl.connect(brokerConnect, port);
			}
		}
		return null;
	}
	
	public void addBroker(Broker broker) {
		this._brokers.add(broker);
	}

}
