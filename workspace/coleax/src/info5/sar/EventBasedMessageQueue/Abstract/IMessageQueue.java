package info5.sar.EventBasedMessageQueue.Abstract;


public interface IMessageQueue {
	    
	    public abstract void setListener(MessageListener listener);
	    public abstract boolean send(byte[] message);
	    public abstract void close();
	    public abstract boolean closed();

}
