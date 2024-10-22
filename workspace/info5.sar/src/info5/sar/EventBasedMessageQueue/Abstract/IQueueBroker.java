package info5.sar.EventBasedMessageQueue.Abstract;


public interface IQueueBroker {
	
	public abstract String name();
    
    public abstract boolean unbind(int port);
    
    public abstract boolean bind(int port, AcceptListener listener);
    
    public abstract boolean connect(String name, int port, ConnectListener listener);

}
