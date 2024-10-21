package info5.sar.EventBasedMessageQueue.Abstract;


public interface IQueueBroker {
	
public abstract String name();
    
    public interface IAcceptListener {
        void accepted(IMessageQueue queue);
    }
    
    public interface IConnectListener {
        void connected(IMessageQueue queue);
        void refused();
    }
    
    public abstract boolean unbind(int port);
    
    public abstract boolean bind(int port, IAcceptListener listener);
    
    public abstract boolean connect(String name, int port, IConnectListener listener);

}
