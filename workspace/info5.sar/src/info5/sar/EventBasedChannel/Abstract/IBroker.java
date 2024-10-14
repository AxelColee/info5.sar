package info5.sar.EventBasedChannel.Abstract;

public interface IBroker {
	
	boolean accept(int port, IAcceptListener listener);
	boolean connect(String name, int port, IConnectListner listener);

}
