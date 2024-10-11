package info5.sar.ThreadedMessageQueue.Impl;

import info5.sar.ThreadedChannel.Abstract.Broker;
import info5.sar.ThreadedChannel.Abstract.Channel;
import info5.sar.ThreadedChannel.Impl.BrokerImpl;
import info5.sar.ThreadedMessageQueue.Abstract.MessageQueue;

public class QueueBroker extends info5.sar.ThreadedMessageQueue.Abstract.QueueBroker{
	
	private BrokerImpl _broker;

	public QueueBroker(Broker broker) {
		super(broker);
		_broker = (BrokerImpl) broker;
	}

	@Override
	public String name() {
		return _broker.getName();
	}

	@Override
	public MessageQueue accept(int port) {
		Channel channel = _broker.accept(port);
		return new info5.sar.ThreadedMessageQueue.Impl.MessageQueue(channel);
	}

	@Override
	public MessageQueue connect(String name, int port) {
		Channel channel = _broker.connect(name, port);
		return new info5.sar.ThreadedMessageQueue.Impl.MessageQueue(channel);
	}
	
	

}
