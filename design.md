# Event Based MessageQueue Design

## Prerequisite
This framework is event based and multithreaded. 

## ThreadedTasks
We will refer as threadedTask when we discuss the Tasks at the top layer of the implenetation. Those tasks have a predeteminded behavior dictated by a Runnable thatt is given in the constructor. They extend thread and so will be started() in main. **Each ThreadedTask runs on a Thread**.

ThreadedTasks are the entitites trying to connect and communicate to each other. **But they will only be able to perform Bind and Connect in their Runnable** (the method available in their Broker). How a Task will repond to a connection or bind that succeds will be set in the different listener that, and will be described later on(ConnectListener, AcceptListener, MessageListener).

**All in all, a Threaded Tasks shouldn't stop its execution at any moment.**


### Attributes

- *private QueueBroker _broker* : The QueueBroker that will initiate connection with other broker and so other ThreadedTasks.

- *private Runnable _runnable* : The runnable of this thread.

### Constructor

Task(QueueBroker b, Runnable r) : Sets _QueueBroker and _runnnable attributes.

### Methods

Broker getBroker() : Returns _broker.

static Task getTask() : Get the current thread, casts it to Task and returns it.

## EventPump
The EventPump is the one that will execute Runnable posted by EventThreads. The runnable that it executes should be short enough to include **one** of the following action :
- send()
- connect()

**A pump will have its own thread** and be be started by the first runnable that's added to its runnables.

**The Pump follows a singleton pattern**

This class **extends thread** so a start method can be called.

### Attributes

- private *Queue< Runnable > _runnables* : A Queue of all the runnable to execute.

- *private Runnable _currentRunnable* : The Runnable being treated in the start loop.

- *private boolean _running* : a flag to dettect if the pump should stop

### Constructor

- *private EventPump()* : Declares _runnables.

### Method

- *syncronized public post(Runnable runnable)* : Adds A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list. **Notifies** when finishes.

- *syncronized public removeRunnable(Runnable runnable)* : Removes A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list.

- *synchronized private Runnable getNextRunnable(Runnable runnable)* : Returns the next Runnable from _runnables.
Is **Synchronized** to allow several threads to use the same list.

- *private void start()* : Loops while (_running) on all the runnables. If one currentRunnable is killed, it will be directly skipped. The Ruunable is removed before of _runnables before being tested. If a Runnable isn't killed at the end of its run function then it will be posted again.

## EventTasks
Event Tasks shouldn't be visible to a user. They operate between QueueBroker, MessageQueue and the pump. QueueBroker will create new EventTask with a runnbale that tries to connect and be sent on the pump. MessageQueue will create new Event task with a runnable that tries to send and receive.

### Attributes

- *private Runnable _runnable* : the runnable asociated to this task.
- *private boolean killed* : A boolean that describes the the state of an EventTask.

### Methods

- *public abstract void post(Runnable r)*  : call the pump *post* method
- *public static EventTask task()* : Returns the pump currentRunnable attribute
- *public abstract void kill()* : Changes the _killed attributes to true.
- *public abstract boolean killed()* :Returns _ killed.

### Specialization

This class should be extended and change for the different event Task possible. For instance, one could be dedicated to connect, bind, send ... Constructor should be thought accordingly.

Each of this version should have a specifific run() method. This method will be posted using the post(Runnable r) method.

**Every Specialization should implements Runnables**

#### BindSpecialization
The AcceptEventTask has the broker  on which it should accepts, the port and the listener associated.

In the run() method, if the _bind method succeds it will kill itself.

#### UnbindSpecialization
The UnbindEventTask has the broker on which it should unbind and the port number.

In the run() method, if the _unbind method succeeds it will kill itself.

#### ConnectSpecialization
The ConnectEventTask has the name on which it should connect, port number and ConnectListener associated.

In the run() method, if the _connect method succeds it will kill itself.

#### SendSpecialization
The SendEventTask has the queue and the Message.

In the run() method, if the _send method succeds it will kill itself.

#### ReceiveSpecialization
The receive specialization has the message queue

In the run() method, if the _receieve method succeds it will kill itself.


## QueueBroker 

QueueBroker should be **synchronized**. Indeed, a queueBroker can be accessed by sevral ThreadedTask at the same time. Therefore different bind method will access the same protected data _accepts (see below). 
The main mission of queue Broker is to to create EventTask designed for each of its actions (more details in methods section)

### Attributes

- *private Map< Integer, AcceptListener > _accepts* : A map linking the port and the associated AcceptListener.

- *private BrokerManager _broker* : The brokerManager it is associated to.

- *private String _name* : The name of this broker.

- *private BrokerManager _brokerManager : The borokerManager instance.

### Constructor

*public QueueBroker(String name)* : Defines the _name. Also sets the unique singleton instance of the BrokerManager and register itself on it and register itself to the brokerManager. Finally initializes the map _accepts.

### Methods

- *public boolean unbind(int port)* : Creates a new task with a runnable calling _unbind(port, this).

- *private _unbind(int port, Queue broker broker)* : 
 removes the entry *port* of *_accepts*.

- *public abstract boolean bind(int port, AcceptListener listener)* : Creates a new task with a runnable that adds a new entry *port*,*listener* on *_accepts* of *broker*.

- *private boolean _bind(int port, AcceptListener listener)* : Adds a new entry on _accepts if it doesn't already exists.

- *public boolean connect(String name, int port, ConnectListener listener, Queue Broker)* : Creates a new EventTask containing a Runnable that tries connecting to the broker *name* on *port* and sends *listener* with it. 

- *private boolean _connect(int port, ConnectListener listner)* : If a match is found  creates new Channel, calls listner.connected(queue1), currentAcceptlistener.Accepted(queue2) with the correct queue and returns true. If not match have been found, returns false.

## MessageQueue
Message queue can be accesed by multiple instances.
Message queue will be accessed by the listnners Accept and Connect Listeners in order to run code once an accept or a connect succeeds.

For message sending and receiving, in the connect and accepted method, a MessageListener should be set. It describes the behavior of this message queue once a message is received or sent.
(For more details about the interfaces go to Intefaces Section).

This class should also create EventTAsk for some actions.

### Attrributes 

- *private MessageListener _messageListener* : The listener for message.

- *private Channel _channel* : Channel from the Channel framework that sends bytes.

- *private Message _msg* : A message instance used only to save parts of the message recieved.

- *private Message _msgLength : A message instance to save the length of the message being received.

### Constructor 

- *public MessageQueue(Channel channel)* : Sets _channel.

### Methods

- *public abstract void setListener(MessageListener listener)* : Sets _listener
    
- *public abstract void send(Message message)* : Creates a new EventTask a sets a runnable that sends _message on a channel. Ownership of messge is given.

-*private boolean _send(Message message)* : If the entire message on channel, returns true else returns false, for a partial sent *message*. It also sets a new send task if not complete.


-*private void _receive(MessageListener listener) : Fills first _msgLength and then _msg. Once msg is complete calls listenr.received(Message msg). Finally it resets both Message attributes.  It also sets a new receive task if not complete.

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

## BrokerManager Class

BrokerManager is the entity that know all of the brokers and allow them to find eachother.

This class follows a **singleton pattern**.

### Attributes

- *private static final BrokerManager INSTANCE* : The singleton instance. 

- *private HashMap<String, QueueBroker> _brokers* : A map of (name,QueueBroker).

### Constructor

- *public BrokerManager()* initializes the _brokers. 

### Methods

- *public void registerBroker(QueueBroker broker)* : adds *broker* to *_brokers* with its name has a key.

- *public QueueBroker getBroker(String name)* : Returns the Broker with the name *name*. If not found null.