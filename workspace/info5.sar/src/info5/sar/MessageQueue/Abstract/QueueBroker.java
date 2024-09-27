package info5.sar.MessageQueue.Abstract;

import info5.sar.Channel.Abstract.Broker;

public abstract class QueueBroker {
	
	public QueueBroker(Broker broker) {}
	public abstract String name();
	public abstract MessageQueue accept(int port);
	public abstract MessageQueue connect(String name, int port);

}
