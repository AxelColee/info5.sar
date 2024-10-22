package info5.sar.EventBasedMessageQueue.Abstract;

public abstract class ConnectListener {
	
	private MessageListener _messageListener;
	
	public ConnectListener(MessageListener messageListener) {
		_messageListener = messageListener;
	}
	
    public abstract void connected(IMessageQueue queue);
    public abstract void refused();
    public MessageListener getMessageListener() {
    	return _messageListener;
    }

}
