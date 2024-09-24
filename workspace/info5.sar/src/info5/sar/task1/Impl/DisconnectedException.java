package info5.sar.task1.Impl;

public class DisconnectedException extends Exception {
	
	public DisconnectedException(String s) {
        super("The channel has been disconnected.");
    }

}
