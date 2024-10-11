package info5.sar.MixedMessageQueue.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.MixedMessageQueue.Impl.Event.BindEvent;
import info5.sar.MixedMessageQueue.Impl.Event.ConnectEvent;
import info5.sar.MixedMessageQueue.Impl.Event.ReceiveEvent;
import info5.sar.MixedMessageQueue.Impl.Event.UnbindEvent;
import info5.sar.ThreadedChannel.Impl.BrokerImpl;
import info5.sar.ThreadedChannel.Impl.ChannelImpl;

public class QueueBroker extends info5.sar.MixedMessageQueue.Abstract.QueueBroker{

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
	
	public void _unbind(int port) {
		this.removeBind(port);

	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		Task task = new Task();
		BindEvent bindEvent = new BindEvent(task, this, port, listener);
		task.post(bindEvent);
		return true;
	}
	
	public void _bind(int port, AcceptListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				do {
					
					addBind(port, listener);
					
					ChannelImpl channelAccept = (ChannelImpl) _broker.accept(port);
					
					MessageQueue mq = new MessageQueue(channelAccept);
					
					listener.accepted(mq);
					
					Task task = new Task();
					ReceiveEvent receievEvent = new ReceiveEvent(task, mq);
					task.post(receievEvent);
					
				}while(getBind(port));
				
			}
		}).start();
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		Task task = new Task();
		ConnectEvent connectEvent = new ConnectEvent(task, this, name, port, listener);
		task.post(connectEvent);
		return true;
	}
	
	
	public void _connect(String name, int port, ConnectListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ChannelImpl channelConnect = (ChannelImpl) _broker.connect(name, port);
				
				if(channelConnect == null) {
					listener.refused();
				}else {
					MessageQueue mq = new MessageQueue(channelConnect);
					listener.connected(mq);
					
					Task task = new Task();
					ReceiveEvent receievEvent = new ReceiveEvent(task, mq);
					task.post(receievEvent);
				}
				
			}
		}).start();
	}

	@Override
	public String name() {
		return _broker.getName();
	}
	
	private void removeBind(int port) {
		synchronized (_binds) {
			_binds.remove(port);
		}
	}
	
	private void addBind(int port, AcceptListener listener) {
		synchronized (_binds) {
			_binds.put(port, listener);
		}
	}
	
	private boolean getBind(int port) {
		synchronized (_binds) {
			return _binds.containsKey(port);
		}
	}
	
	private BrokerImpl getBroker() {
		return _broker;
	}

}
