package info5.sar.task1.Impl;

import info5.sar.task1.Broker;
import info5.sar.task1.Channel;

public class Rdv {

	private int _port;
	private BrokerImpl _brokerAccept, _brokerConnect;
	private Channel _channelAccept, _channelConnect;
	
	public Rdv(int port) {
		this._port = port;
	}
	
	@SuppressWarnings("static-access")
	synchronized public Channel connect(BrokerImpl brokerConnect){
		
		this._brokerConnect = brokerConnect;
		
		if(this._brokerAccept != null) {
			this.newChannels();
			
			notifyAll();
		}else {
			try {
				while(this._brokerAccept == null) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this._channelConnect;
		
	}
	
	@SuppressWarnings("static-access")
	synchronized public Channel accept(BrokerImpl brokerAccept){
		
		this._brokerAccept = brokerAccept;
		
		if(this._brokerConnect != null) {
			this.newChannels();
			
			notifyAll();
			
		}else {
			try {
				while(_brokerConnect == null) {
					wait();

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		_brokerAccept.getRdvs().remove(this);
		return this._channelAccept;
		
	}
	
	private void newChannels() {
		CircularBuffer in = new CircularBuffer(100);
		CircularBuffer out = new CircularBuffer(100);
		
		this._channelAccept = new ChannelImpl(in, out);
		this._channelConnect = new ChannelImpl(out, in);
	}
	
	public int getPort() {
		return this._port;
	}
	
	public boolean isAccept() {
		return this._brokerAccept != null;
	}
	
	public boolean isConnect() {
		return this._brokerConnect != null;
	}
	
	
}
