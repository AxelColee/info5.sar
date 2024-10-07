package info5.sar.EventBasedMessageQueue.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.BindEventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.ConnectEventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.EventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.ReceiveEventTask;
import info5.sar.EventBasedMessageQueue.Impl.EventTasks.UnbindEventTask;

public class QueueBroker extends info5.sar.EventBasedMessageQueue.Abstract.QueueBroker{
	
	private BrokerManager _brokerManager;
	private String _name;
	private Map<Integer,AcceptListener> _accepts;

	public QueueBroker(String name) {
		super(name);
		_name = name;
		_brokerManager = BrokerManager.getInstance();
		_brokerManager.registerBroker(this);
		_accepts = new HashMap<Integer, QueueBroker.AcceptListener>();
	}

	@Override
	public boolean unbind(int port) {
		EventTask task = new UnbindEventTask(this, port);
		return true;
	}
	
	public boolean _unbind(int port) {
		synchronized (_brokerManager) {
			_accepts.remove(port);
			return true;
		}
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		EventTask task = new  BindEventTask(this, port, listener);
		return true;
	}
	
	public boolean _bind(int port, AcceptListener listener) {
		synchronized (_accepts) {
			if(_accepts.containsKey(port)) {
				return false;
			}
			_accepts.put(port, listener);
			return true;
		}
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		ConnectEventTask task = new ConnectEventTask(name, port, listener);
		return true;
	}
	
	public boolean _connect(int port, ConnectListener listener) {
		synchronized (_accepts) {
			if(_accepts.containsKey(port)) {
				
				ChannelImpl channel1 = new ChannelImpl();
				ChannelImpl channel2 = new ChannelImpl();
				
				channel1._out = channel2._in;
				channel2._out = channel1._in;
				
				channel1._rch = channel2;
				channel2._rch = channel1;
				
				MessageQueue queue1 = new MessageQueue(channel1);
				MessageQueue queue2 = new MessageQueue(channel2);
				
				listener.connected(queue1);
				_accepts.get(port).accepted(queue2);
				
				EventTask task1 = new ReceiveEventTask(queue1);
				EventTask task2 = new ReceiveEventTask(queue2);
				
				return true;
			}
			return false;
		}
	}

}
