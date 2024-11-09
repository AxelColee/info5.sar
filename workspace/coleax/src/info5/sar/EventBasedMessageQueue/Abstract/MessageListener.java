package info5.sar.EventBasedMessageQueue.Abstract;

/**
 * Abstract class representing a message listener that handles events from a message queue.
 * Implementations of this class should define the behavior when a message is received,
 * when a connection is closed, and when a message is sent.
 * 
 * @see IMessageQueue
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #received(byte[])}: Called when a message is received.</li>
 * <li>{@link #closed()}: Called when a connection is closed.</li>
 * <li>{@link #sent(byte[])}: Called when a message is sent.</li>
 * </ul>
 */
public abstract class MessageListener {
	
		protected IMessageQueue _queue;
		
		public MessageListener(IMessageQueue queue) {
			_queue = queue;
		}
	
        public abstract void received(byte[] bytes);
        public abstract void closed();
        public abstract void sent(byte[] message);
        
}
