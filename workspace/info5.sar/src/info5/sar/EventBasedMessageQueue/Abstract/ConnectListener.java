package info5.sar.EventBasedMessageQueue.Abstract;

public abstract class ConnectListener {
	
    public abstract void connected(IMessageQueue queue);
    public abstract void refused();

}
