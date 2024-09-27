package info5.sar.Channel.Impl;

import info5.sar.Channel.Abstract.Broker;
import info5.sar.Channel.Abstract.Channel;

public class Rdv {

	private int _port;
	private Broker _brokerAccept, _brokerConnect;
	private ChannelImpl _channelAccept, _channelConnect;
	
	public Rdv(int port) {
		this._port = port;
	}
	
	
	synchronized public Channel connect(){
				
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
	
	synchronized public Channel accept(){
				
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
		return this._channelAccept;
		
	}
	
	private void newChannels() {
		
		this._channelAccept = new ChannelImpl();
		this._channelConnect = new ChannelImpl();
		
		this._channelAccept._out = this._channelConnect._in;
		this._channelConnect._out = this._channelAccept._in;
		
		this._channelAccept._rch = this._channelConnect;
		this._channelConnect._rch = this._channelAccept;
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
	
	public void setConnectBroker(Broker b) {
		this._brokerConnect = b;
	}
	
	public void setAcceptBroker(Broker b) {
		this._brokerAccept = b;
	}
	
	
}
