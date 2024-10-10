package info5.sar.EventBasedMessageQueue.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.Channel.Impl.BrokerImpl;
import info5.sar.EventBasedMessageQueue.Impl.Event.BindEvent;
import info5.sar.EventBasedMessageQueue.Impl.Event.ConnectEvent;
import info5.sar.EventBasedMessageQueue.Impl.Event.UnbindEvent;

public class QueueBroker extends info5.sar.EventBasedMessageQueue.Abstract.QueueBroker{

	private BrokerImpl _broker;
	private Map<Integer,AcceptListener> _binds;
	
	public QueueBroker(String name) {
		super(name);
		_broker = new BrokerImpl(name);
		_binds = new HashMap<Integer, QueueBroker.AcceptListener>();
	}

	@Override
	public boolean unbind(int port) {
		Task task = new Task();
		UnbindEvent unbindEvent = new UnbindEvent(task, this, port);
		task.post(unbindEvent);
		return true;
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		Task task = new Task();
		BindEvent bindEvent = new BindEvent(task, this, port, listener);
		task.post(bindEvent);
		return true;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		Task task = new Task();
		ConnectEvent connectEvent = new ConnectEvent(task, _broker, name, port, listener);
		task.post(connectEvent);
		return true;
	}

	@Override
	public String name() {
		return _broker.getName();
	}
	
	public void removeBind(int port) {
		synchronized (_binds) {
			_binds.remove(port);
		}
	}
	
	public void addBind(int port, AcceptListener listener) {
		synchronized (_binds) {
			_binds.put(port, listener);
		}
	}
	
	public boolean getBind(int port) {
		synchronized (_binds) {
			return _binds.containsKey(port);
		}
		
	}
	
	public BrokerImpl getBroker() {
		return _broker;
	}

}
