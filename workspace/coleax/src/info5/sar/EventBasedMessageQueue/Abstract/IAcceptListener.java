package info5.sar.EventBasedMessageQueue.Abstract;

public abstract interface IAcceptListener {
	
    public abstract void accepted(IMessageQueue queue);
   
}
