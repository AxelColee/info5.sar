package info5.sar.EventBasedChannel.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.EventBasedChannel.Abstract.IAcceptListener;
import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Abstract.IConnectListener;

public class Broker implements IBroker{
	
	private BrokerManager _brokerManager;
	private String _name;
	private Map<Integer,IAcceptListener> _binds;

	public Broker(String name) {
		_name = name;
		_brokerManager = BrokerManager.getInstance();
		_brokerManager.registerBroker(this);
		_binds = new HashMap<Integer, IAcceptListener>();
	}

	@Override
	public boolean unbind(int port) {
		
		if(!_binds.containsKey(port)) {
			return false;
		}
		
		_binds.remove(port);
		return true;
	}

	@Override
	public boolean bind(int port, IAcceptListener listener) {
		if(_binds.containsKey(port)) {
			return false;
		}
		_binds.put(port, listener);
		return true;
	}

	@Override
	public boolean connect(String name, int port, IConnectListener listener) {
		Broker broker = _brokerManager.getBroker(name);
		if(broker == null) {
			listener.refused();
			return false;
		}else {
			broker._connect(port, listener);
			return true;
		}
		
	}
	
	private void _connect(int port, IConnectListener listener) {
		if(_binds.containsKey(port)) {
			
			Channel channelAccept = new Channel();
			Channel channelConnect = new Channel();
			
			channelAccept._out = channelConnect._in;
			channelConnect._out = channelAccept._in;
			
			channelAccept._rch = channelConnect;
			channelConnect._rch = channelAccept;
			
			listener.connected(channelConnect);
			_binds.get(port).accepted(channelAccept);
							
		}else {
			new Task().post(() -> _connect( port, listener));
		}
		
	}
	
	public String name() {
		return _name;
	}
}
