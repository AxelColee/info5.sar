package info5.sar.task1.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;
import info5.sar.task1.Task;

public class BrokerImpl extends info5.sar.task1.Broker{
	
	public String _name; 
	private BrokerManager _brokerManager;
	private List<Rdv> _rdvs;

	public BrokerImpl(String name, BrokerManager brokerManager) {
		super(name);
		this._name = name;
		this._brokerManager = brokerManager;
		this._rdvs = new LinkedList<Rdv>();
		
		brokerManager.addBroker(this);
	}

	@Override
	public Channel accept(int port) {
		Rdv rdv;
		synchronized (_rdvs) {
			for(Rdv r: this._rdvs) {
				if(r.isAccept()) {
					throw new IllegalStateException("Two Accepts on the same Broker");
				}
				if(r.isConnect() && r.getPort() == port) {
					return r.accept(this);
				}
			}
			
			rdv = new Rdv(port);
			this._rdvs.add(rdv);
		}
		return rdv.accept(this);
		
	}

	@Override
	public Channel connect(String name, int port) {
		return this._brokerManager.connect(this, name, port);
	}
	
	Channel connect(BrokerImpl brokerConnect, int port) {
		Rdv rdv;
		synchronized (_rdvs) {
			for(Rdv r: this._rdvs) {
				if(r.isAccept() && r.getPort() == port) {
					return r.connect(brokerConnect);
				}
			}
			
			rdv = new Rdv(port);
			this._rdvs.add(rdv);

		}
		
		return rdv.connect(this);

		
		
	}
	
	public Task getTask() {
		return (Task) Thread.currentThread();
	}
	
	public String getName() {
		return this._name;
	}
	
	public List<Rdv> getRdvs() {
		return _rdvs;
	}


}
