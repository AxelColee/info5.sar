package info5.sar.EventBasedMessageQueue.Impl.Event;

import info5.sar.Channel.Impl.BrokerImpl;
import info5.sar.Channel.Impl.ChannelImpl;
import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.ConnectListener;
import info5.sar.EventBasedMessageQueue.Impl.MessageQueue;
import info5.sar.EventBasedMessageQueue.Impl.Task;

public class ConnectEvent extends Event{
	
	private BrokerImpl _broker;
	private String _name; 
	private int _port;
	private ConnectListener _listener;
	
	public ConnectEvent(Task task, BrokerImpl broker, String name, int port, ConnectListener listener) {
		super(task);
		_broker = broker;
		_name = name;
		_port = port;
		_listener = listener;
	}

	@Override
	protected void toDo() {
		ChannelImpl channelConnect = (ChannelImpl) _broker.connect(_name, _port);
		
		if(channelConnect == null) {
			_listener.refused();
		}else {
			MessageQueue mq = new MessageQueue(channelConnect);
			_listener.connected(mq);
			
			Task task = new Task();
			ReceiveEvent receievEvent = new ReceiveEvent(task, mq, mq.getListener());
			task.post(receievEvent);
		}
		

		
	}

}
