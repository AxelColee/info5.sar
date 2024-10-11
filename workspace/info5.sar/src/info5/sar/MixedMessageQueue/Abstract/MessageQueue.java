package info5.sar.MixedMessageQueue.Abstract;

import info5.sar.MixedMessageQueue.Impl.Message;

public abstract class MessageQueue {
	public interface MessageListener {
        void received(byte[] bytes);
        void closed();
        void sent(Message message);
    }
    
    public abstract void setListener(MessageListener listener);
    public abstract void send(Message message);
    public abstract void close();
    public abstract boolean closed();
}
