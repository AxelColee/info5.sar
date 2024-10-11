package info5.sar.MixedMessageQueue.Impl;

import java.util.HashMap;
import java.util.Map;

import info5.sar.ThreadedChannel.Impl.BrokerImpl;
import info5.sar.ThreadedChannel.Impl.ChannelImpl;

public class QueueBroker extends info5.sar.MixedMessageQueue.Abstract.QueueBroker{

	private BrokerImpl _broker;
	private Map<Integer,Thread> _bindThreads;
	
	public QueueBroker(String name) {
		super(name);
		_broker = new BrokerImpl(name);
		_bindThreads = new HashMap<Integer, Thread>();
	}
	
	public boolean unbind(int port) {
		if(_bindThreads.containsKey(port)) {
			Thread t = _bindThreads.get(port);
			t.interrupt();
			removeBind(port);
		}
		return true;
	}
	
	public boolean bind(int port, AcceptListener listener) {
		Thread thread = new Thread();
	
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				do {
										
					ChannelImpl channelAccept = (ChannelImpl) _broker.accept(port);
					if(channelAccept != null) {
						MessageQueue mq = new MessageQueue(channelAccept);
						
						
						
						Task task = new Task();
						task.post(() -> listener.accepted(mq));
					}
					
				}while(getBind(port));
				
			}
		});
		addBind(port, thread);
		thread.start();
		return true;
	}	
	
	public boolean connect(String name, int port, ConnectListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ChannelImpl channelConnect = (ChannelImpl) _broker.connect(name, port);
				
				Task task = new Task();

				
				if(channelConnect == null) {
					task.post(() -> listener.refused());
				}else {
					MessageQueue mq = new MessageQueue(channelConnect);					
					task.post(() -> listener.connected(mq));
				}
				
			}
		}).start();
		return true;
	}

	@Override
	public String name() {
		return _broker.getName();
	}
	
	private void removeBind(int port) {
		synchronized (_bindThreads) {
			_bindThreads.remove(port);
		}
	}
	
	private void addBind(int port, Thread thread) {
		synchronized (_bindThreads) {
			_bindThreads.put(port, thread);
		}
	}
	
	private boolean getBind(int port) {
		synchronized (_bindThreads) {
			return _bindThreads.containsKey(port);
		}
	}
	
	private BrokerImpl getBroker() {
		return _broker;
	}

}
