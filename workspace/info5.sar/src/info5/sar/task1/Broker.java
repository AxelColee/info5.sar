package info5.sar.task1;

public abstract class Broker {

	 public Broker(String name) {} 
	 
	 /**
	  * 
	  * @param port
	  * @return
	  */
	 public abstract Channel accept(int port);
	 
	 /**
	  * 
	  * @param name
	  * @param port
	  * @return
	  */
	 public abstract Channel connect(String name, int port);
}
