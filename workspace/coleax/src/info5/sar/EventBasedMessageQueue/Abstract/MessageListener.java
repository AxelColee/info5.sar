package info5.sar.EventBasedMessageQueue.Abstract;

public abstract class MessageListener {
	
		protected IMessageQueue _queue;
		
		public MessageListener(IMessageQueue queue) {
			_queue = queue;
		}
	
        public abstract void received(byte[] bytes);
        public abstract void closed();
        public abstract void sent(byte[] message);
        
}
