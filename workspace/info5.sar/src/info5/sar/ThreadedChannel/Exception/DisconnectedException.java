package info5.sar.ThreadedChannel.Exception;

public class DisconnectedException extends Exception {
	
	public DisconnectedException(String s) {
        super("The channel has been disconnected.");
    }

}
