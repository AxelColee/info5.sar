package info5.sar.EventBasedMessageQueue.Abstract;

import info5.sar.EventBasedMessageQueue.Impl.Message;

public abstract class MessageListener {
	
		protected IMessageQueue _queue;
	
        public abstract void received(byte[] bytes);
        public abstract void closed();
        public abstract void sent(Message message);
        
        public void setQueue(IMessageQueue queue) {
        	_queue = queue;
        }

}
