package info5.sar.task1.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;
import info5.sar.task1.Task;

public class BrokerImpl extends info5.sar.task1.Broker{
	
	public String _name; 
	private BrokerManager _brokerManager;
	private List<Rdv> _rdvs; //Beteer to use a HashMap

	public BrokerImpl(String name, BrokerManager brokerManager) { ///Get the broker manager in the constructor not given in the constructor
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
			//_rdvs.notifyAll();
		}
		return rdv.accept(this);
		
	}

	@Override
	public Channel connect(String name, int port) {
		return this._brokerManager.connect(this, name, port);
	}
	
	
	
	/// An other way is to make all the connect wait here. Let only the first one pass to rdv. 
	//Juste le notify dans e accept (seul un nouveau accept permet au connect d'avoir un rendez vous 
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
