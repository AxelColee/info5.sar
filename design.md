# Event Based MessageQueue Design

## Prerequisite
This framework is event based and multithreaded. 

## Task
Task objects allows to post runnables on the EventPump

### Attributes

- *private EventPump _pump* The pump on which Runnab les will be posted

- *static Task _currentTask* : The task being run by the pump.

- *private Runnable _runnable* : the runnable this task has to post.

- *private boolean _killed : Wheter or not this task has been killed.

## Constructor

- *public Task()* : Sets pump with the su=ingleton insatnce of it and *_killed* to false.

### Methods

- *public abstract void post(Runnable r) : _pump.post(r) and sets _runnable.

- *public static EventTask task()* : Returns _currentTask.

- *public abstract void kill()* : _killed = true

- *public abstract boolean killed()* : Returns _killed


## EventPump
The EventPump is the one that will execute Runnable posted by tasks. 

**A pump will have its own thread** and be be started in the boostraping so it **extends Thread**

**The Pump follows a singleton pattern**

### Attributes

- private *Queue< Runnable > _runnables* : A Queue of all the runnable to execute.

- *private Runnable _currentRunnable* : The Runnable being treated currently.

- *private boolean _running* : a flag to detect if the pump should stop.

### Constructor

- *private EventPump()* : Declares *_runnables* and sets *_runnning* to true

### Method

- *syncronized public post(Runnable runnable)* : Adds A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list. **Notifies** when finishes.

- *syncronized public removeRunnable(Runnable runnable)* : Removes A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list.

- *synchronized private Runnable getNextRunnable(Runnable runnable)* : Returns the next Runnable from *_runnables ands* sets *_currentRunnable*
Is **Synchronized** to allow several threads to use the same list.

- *private void run()* : Loops while (_running) on all the runnables. The Ruunable is removed before being treated. If *_runnables* is empty waits until a new runnable is posted.

## QueueBroker 
QueueBroker should synchronize all of its access to _binds lists.
It will iteract with the Channel Framework being threaded. It should never call directly _broker as it can be blocked. Therefore each time it wants to access _broker a new Thread has to be created to perform the method.

### Attributes

- *private Map< Integer, AcceptListener > _binds* : A map linking the port and the associated AcceptListener. It represents all the bind currently being done.

- *private Broker _broker* : The broker.

- *private BrokerManager _brokerManager : The borokerManager instance.

### Constructor

*public QueueBroker(String name)* : Creates a new Broker(*name*). Finally initializes the map _binds.

### Methods

- *public boolean unbind(int port)* : Creates a new unbindEvent and commision a thread to run it. Returns true if sent on the pump (a bind on *port*) exists, false otherwise.

- *public abstract boolean bind(int port, AcceptListener listener)* : Creates a new bindEvent and commision a thread to run it. Returns true if no other bind on the same port already exists or the number of binds hasn't exceeds, false otherwise.

- *public boolean connect(String name, int port, ConnectListener listener)* : Creates a new connectEvent and commision a thread to run it. Returns true if sent on the pump.

## MessageQueue
MessageQueue as Queue Broker should commission thread to run any method that could use its _channel attributes as its methods can be blocking.
It's the responsability of the user to set correclty the MessageListener of this Queue other wise an IllegalStateException should be raised.

### Attrributes 

- *private MessageListener _messageListener* : The listener for message.

- *private Channel _channel* : Channel from the Channel framework that sends bytes.

### Constructor 

- *public MessageQueue(Channel channel)* : Sets _channel.

### Methods

- *public abstract void setListener(MessageListener listener)* : Sets _listener
    
- *public abstract void send(Message message)* : Creates a new sendEvent and commision a thread to do it.

-*private void _receive(MessageListener listener) : Creates a new sendEvent and commision a thread to do it.

- *public abstract void close()* : Calls disconnect on _channel.

- *public abstract boolean closed()* : Returns _channel _disconnected attribute.

## AcceptListener
Accept Listener is a an interface.
It describes the behavior one a Broker has accepted a connection.

Several implementation of thos interface should exist to caracterise the behavior wanted.

### Method
- *void accepted(MessageQueue queue)* : *queue* is the new messsage queue created after acceptance of the connection. Implementation should be specific but is should usually include queue.setListener() to define the behavoir once message are sent or recieved.

## ConnectListener 
Connect Listener is an interface that describes the possible behavior after a connect has been sent by a broker.

ConnectListener implementation should be specific to your usage of this framework.

### Methods

- *public void connected(MessageQueue queue)* : called once two brokers are indeed connected. Implentation details won't be discusses as it is specific to your need. But i should usually include queue.setListener(messageListener) to set behavoir for message exchanging.

- *public refused()* : Your behavior for a connection refused.

## Event 

Event is an abstract class that implements Runnable. They will be the specific runnables runned by QueueBroker And message Queue for their respective actions

### UnbindEvent

#### Attributes
- private QueueBroker _queueBroker : The queueBroker from which this runnable has been emitted.
- private int _port : the port on which it shoul unbind.

#### Constructor

- *public UnbindEvent(QueueBroker queue, int port)* : Sets _queueBroker and _port 

#### Methods

- *public void run()* : Goes through _binds and removes the *_port* entry if it exists.

### BindEvent

#### Attributes
- *private Broker _broker : the broker on which it should accept
- private int _port : the port on which the broker should accept
- private AcceptListenr listener : The listener once it has bound.

#### Constructor

- *public UnbindEvent(Broker broker, int port, AcceptListener listener)* : Sets all the attributes

#### Methods

- *public void run()* : Calls accepts(port) on _broker. The channel returned will be used to create a new Message Queue instance and finally accepted() methods of the listener will be invoked with the queue. Loops on this while the bond entry exists in _binds.

### ConnectEvent

#### Attributes
- *private Broker _broker : the broker on which it should accept
- private String _name : The broke namer on it shoudl accept.
- private int _port : the port on which the broker should accept
- private ConectListenr listener : The listener once it has bound.

#### Constructor

- *public UnbindEvent(Broker broker, int port, String name, ConnectListener listener)* : Sets all the attributes

#### Methods

- *public void run()* : Calls connects(name, port) on _broker. The channel returned will be used to create a new Message Queue instance and finally accepted() methods of the listener will be invoked with the queue.

### SendEvent

#### Attributes
- *private Broker _channel : the channel on which it should send bytes
- private Message _name : The message that should be sent
- private MessaeListenerListenr listener : The listener for the queue that has emitted this send task.

#### Constructor

- *public SendEvent(Channel channel, Message msg, MessageListener listener)* : Sets all the attributes

#### Methods

- *public void run()* : calls write(msg.getBytes(), offset, length) on _channel. Finally calls sent(msg) on listenrr

### ReceiveEvent

#### Attributes
- *private Broker _channel : the channel on which it should send bytes
- private Message _name : The message that should be sent
- private MessaeListenerListenr listener : The listener for the queue that has emitted this send task.

#### Constructor

- *public ReceiveEvent(Channel channel, MessageListener listener)* : Sets all the attributes

#### Methods

- *public void run()* : calls read(bytes, offset, length) on _channel. Finally calls received(bytes) on listenr