package info5.sar.EventBasedMessageQueue.Impl;


import info5.sar.EventBasedChannel.Abstract.IBroker;
import info5.sar.EventBasedChannel.Impl.Broker;
import info5.sar.EventBasedMessageQueue.Abstract.IAcceptListener;
import info5.sar.EventBasedMessageQueue.Abstract.IConnectListener;
import info5.sar.EventBasedMessageQueue.Abstract.IQueueBroker;

public class QueueBroker implements IQueueBroker{
	
	private IBroker _broker;
	
	public QueueBroker(String name) {
		_broker = (Broker) new Broker(name);
	}

	@Override
	public String name() {
		return _broker.name();
	}

	@Override
	public boolean unbind(int port) {
		return _broker.unbind(port);
	}

	@Override
	public boolean bind(int port, IAcceptListener listener) {
		return _broker.bind(port, new InternalAcceptListener(listener));
	}

	@Override
	public boolean connect(String name, int port, IConnectListener listener) {
		return _broker.connect(name, port, new InternalConnectListener(listener));
	}

}
