package info5.sar.EventBasedMessageQueue.Impl.EventTasks;

import info5.sar.EventBasedMessageQueue.Abstract.QueueBroker.ConnectListener;
import info5.sar.EventBasedMessageQueue.Impl.BrokerManager;
import info5.sar.EventBasedMessageQueue.Impl.QueueBroker;

public class ConnectEventTask extends EventTask{
	
	private String _brokerName;
	private int _port;
	private ConnectListener _listener;
	
	public ConnectEventTask(String brokerName, int port, ConnectListener listener) {
		super();
		_brokerName = brokerName;
		_port = port;
		_listener = listener;
		super.post(this);

	}

	@Override
	public void run() {
		QueueBroker broker = BrokerManager.getInstance().getBroker(_brokerName);
		if(broker == null) {
			_listener.refused();
			this.kill();
		}else {
			if(broker._connect(_port, _listener)) {
				this.kill();
			}
		}
		
	}

}
