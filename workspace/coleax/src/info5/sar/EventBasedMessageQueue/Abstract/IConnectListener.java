package info5.sar.EventBasedMessageQueue.Abstract;

public abstract interface IConnectListener {
	
    public abstract void connected(IMessageQueue queue);
    public abstract void refused();

}
