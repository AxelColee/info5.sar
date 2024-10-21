package info5.sar.EventBasedMessageQueue.Abstract;

import info5.sar.EventBasedMessageQueue.Impl.Message;

public interface IMessageQueue {
	
		public interface IMessageListener {
	        void received(byte[] bytes);
	        void closed();
	        void sent(Message message);
	    }
	    
	    public abstract void setListener(IMessageListener listener);
	    public abstract boolean send(Message message);
	    public abstract void close();
	    public abstract boolean closed();

}
