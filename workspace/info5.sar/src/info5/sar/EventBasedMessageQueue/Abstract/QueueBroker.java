package info5.sar.EventBasedMessageQueue.Abstract;

public abstract class QueueBroker {
	
	private String _name;
	
	public QueueBroker(String name) {
		_name = name;
	}
	
	public  String name() {
		return _name;
	}
    
    public interface AcceptListener {
        void accepted(MessageQueue queue);
    }
    
    public interface ConnectListener {
        void connected(MessageQueue queue);
        void refused();
    }
    
    public abstract boolean unbind(int port);
    
    public abstract boolean bind(int port, AcceptListener listener);
    
    public abstract boolean connect(String name, int port, ConnectListener listener);

}
