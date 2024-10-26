package info5.sar.EventBasedMessageQueue.Abstract;

import info5.sar.EventBasedMessageQueue.Impl.Message;

public interface IMessageQueue {
	    
	    public abstract void setListener(MessageListener listener);
	    public abstract boolean send(Message message);
	    public abstract void close();
	    public abstract boolean closed();

}
