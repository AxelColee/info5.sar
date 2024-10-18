package info5.sar.EventBasedChannel.Abstract;

public interface IBroker {

	boolean bind(int port, IAcceptListener listener);
	boolean unbind(int port);
	boolean connect(String name, int port, IConnectListner listener);

}
