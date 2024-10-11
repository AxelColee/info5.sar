package info5.sar.MixedMessageQueue.Impl;

import java.util.LinkedList;
import java.util.Queue;

import info5.sar.ThreadedChannel.Exception.DisconnectedException;
import info5.sar.ThreadedChannel.Impl.ChannelImpl;

public class MessageQueue extends info5.sar.MixedMessageQueue.Abstract.MessageQueue{

	private boolean _closed;
	private ChannelImpl _channel;
	private MessageListener _listener;
	private Queue<Message> _messages;
	private Thread sendThread;
	
	public MessageQueue(ChannelImpl channel) {
		super();
		_channel = channel;
		_closed = false;
		sendThread = new Thread();
		_messages = new LinkedList<Message>();
		_receive();
		startSendThread();
	}
	
	@Override
	public void setListener(MessageListener listener) {
		_listener = listener;
	}
	
	public void send(Message msg) {
		synchronized (sendThread) {
	        _messages.add(msg);
	        sendThread.notify();
	    }
	}
	
	public void startSendThread() {
		sendThread = new Thread(() -> {
			while (!_closed) {
				while (_messages.isEmpty() && !closed()) {
					try {
						synchronized (sendThread) {
							sendThread.wait();
						}
						
					} catch (InterruptedException e) {
						return;
					}
					if (_closed)
						return;
				}

				Message msg = _messages.poll();
				_send(msg);
			}
		});
		sendThread.start();
	}
	
	private void _send(Message msg) {
		
				int nbSentBytes = 0;
				int length = msg.getLength();
				
				byte[] size = new byte[4];
			    size[0] = (byte) ((length  & 0xFF000000) >> 24);
			    size[1] = (byte) ((length  & 0x00FF0000) >> 16);
			    size[2] = (byte) ((length  & 0x0000FF00) >> 8);
			    size[3] = (byte) (length  & 0x000000FF);
			    
			    
			    
			    while (nbSentBytes < 4) {
			    	try {
						nbSentBytes = _channel.write(size, 0 + nbSentBytes, 4 - nbSentBytes);
					} catch (DisconnectedException e) {
						e.printStackTrace();
					}
				} 
			    
				
				nbSentBytes = 0;

				while(nbSentBytes < length) {
					try {
						nbSentBytes += _channel.write(msg.getBytes(), msg.getOffset() + nbSentBytes, msg.getLength()-nbSentBytes);
					} catch (DisconnectedException e) {
						e.printStackTrace();
					}
				}
				
				Task task = new Task();
				task.post(()-> _listener.sent(msg));

	}
	
	private void _receive() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				do{
					int nbReceivedBytes = 0;
					
					byte[] size = new byte[4];
				    
					while (nbReceivedBytes < 4) {
				    	try {
				    		nbReceivedBytes = _channel.read(size, 0 + nbReceivedBytes, 4 - nbReceivedBytes);
						} catch (DisconnectedException e) {
							//e.printStackTrace();
							return;
						}
					} 
				    
				    int length = ((size[0] & 0xFF) << 24) |
			                ((size[1] & 0xFF) << 16) |
			                ((size[2] & 0xFF) << 8) |
			                (size[3] & 0xFF);
				    
				    byte[] message = new byte[length];
				    
				    nbReceivedBytes = 0;
					while(nbReceivedBytes < length) {
						try {
							nbReceivedBytes += _channel.read(message, 0 + nbReceivedBytes, length-nbReceivedBytes);
						} catch (DisconnectedException e) {
							//e.printStackTrace();
							return;
						}
					}
					
					Task task = new Task();
					task.post(() -> _listener.received(message));
					
				}while(!MessageQueue.this.closed()) ;
				
			}
		}).start();
		
	}
		


	@Override
	public void close() {
		_channel.disconnect();
		sendThread.interrupt();
	}

	@Override
	public boolean closed() {
		return _closed;
	}
	

}
