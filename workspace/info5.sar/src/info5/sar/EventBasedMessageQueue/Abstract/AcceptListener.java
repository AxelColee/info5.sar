package info5.sar.EventBasedMessageQueue.Abstract;

public abstract class AcceptListener {
	
	protected MessageListener _messageListener;
	
	public AcceptListener(MessageListener messageListener) {
		_messageListener = messageListener;
	}
	
    public abstract void accepted(IMessageQueue queue);
    
    public MessageListener getMessageListener() {
    	return _messageListener;
    }

}
