package info5.sar.Channel.Impl;

import java.util.LinkedList;
import java.util.List;

import info5.sar.Channel.Abstract.Broker;
import info5.sar.Channel.Abstract.Channel;
import info5.sar.Channel.Abstract.Task;

public class BrokerImpl extends Broker{
	
	private String _name; 
	private BrokerManager _brokerManager;
	private List<Rdv> _rdvs;

	public BrokerImpl(String name) {
		super(name);
		this._name = name;
		this._brokerManager = BrokerManager.getInstance();
		this._rdvs = new LinkedList<Rdv>();
		
		_brokerManager.addBroker(this);
	}

	@Override
	public Channel accept(int port) {
		Rdv rdv = null;
		synchronized (_rdvs) {
			for(Rdv r: this._rdvs) {
				if(r.isAccept()) {
					throw new IllegalStateException("Two Accepts on the same Broker");
				}
				if(r.isConnect() && r.getPort() == port) {
					rdv = r;
					rdv.setAcceptBroker(this);
					break;
				}
			}
			
			if(rdv == null) {
				rdv = new Rdv(port);
				rdv.setAcceptBroker(this);
				this._rdvs.add(rdv);
			}else {
				_rdvs.remove(rdv);
			}

		}
		return rdv.accept();
		
	}

	@Override
	public Channel connect(String name, int port) {
		
		BrokerImpl remoteBroker = (BrokerImpl) _brokerManager.getBroker(name);
		if(remoteBroker == null) {
			return null;
		}
		
		return remoteBroker.connect(this, port);
	}
	
	
	Channel connect(Broker brokerConnect, int port) {
		Rdv rdv = null;
		synchronized (_rdvs) {
			for(Rdv r: this._rdvs) {
				if(r.isAccept() && r.getPort() == port) {
					rdv = r;
					rdv.setConnectBroker(brokerConnect);
					break;
				}
			}
			
			if(rdv == null) {
				rdv = new Rdv(port);
				rdv.setConnectBroker(brokerConnect);
				this._rdvs.add(rdv);
			}else {
				_rdvs.remove(rdv);
			}
	
		}
		
		return rdv.connect();
		
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
