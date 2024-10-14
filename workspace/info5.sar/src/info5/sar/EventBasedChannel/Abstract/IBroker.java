package info5.sar.EventBasedChannel.Abstract;

public interface IBroker {
	
	boolean accept(int port);
	boolean connect(String name, int port);

}
