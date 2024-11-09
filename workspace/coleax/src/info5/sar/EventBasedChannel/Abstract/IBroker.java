package info5.sar.EventBasedChannel.Abstract;

/**
 * The IBroker interface defines the contract for a broker that handles binding,
 * unbinding, and connecting operations. Implementations of this interface are
 * expected to manage network connections and communication channels.
 */
public interface IBroker {

	String name();
	boolean bind(int port, IAcceptListener listener);
	boolean unbind(int port);
	boolean connect(String name, int port, IConnectListener listener);

}
