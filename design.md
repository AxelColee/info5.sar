# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Binding/Unbinding/connecting
Binding on a broker will accept all the connection on the specified port until an unbind is posted.

## Disconnecting
If the local channel disconnects will reject all of the read and write actions. If actions already accepted are supposed to return a result they will be terminated. Meaning, theses actions could have been interrupted and could be false.
If the remote channel disconnects all the write actions will be performed as usual even if they don't reach the remote channel. Else, read will continue as usual.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even thoug a broker can be used by multiple task instances it will not be synchronized.

### Attributes

- *private Map< Integer, AcceptListener > _accepts* : A map linking the port and the associated AcceptListener.

- *private BrokerManager _broker* : The brokerManager it is associated to.

- *private String _name* : The name of this broker.

- *private BrokerManager _brokerManager : The borokerManager instance.

### constructor
- **Broker(String name)** :
    - *name* : Defines the _name. Also sets the unique singleton instance of the BrokerManager and register itself on it and register itself to the brokerManager. Finally initializes the map _accepts.


- **boolean Channel bind(int port)** : Binds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **boolean Channel unbind(int port)** : Unbinds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **boolean Channel connect(String name, int port);** Tries connecting to an other broker. If the action is posted, will return true. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *name* : The name of the task it tries to connect
    - *port* : The *port* of *name* on which the connection will be established 

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is FIFO and lossless.
Multiple entities writing at the same time won't ensure data consistency.

- **boolean read(bytes[] bytes, int offset, int length);** : Returns true if the read can be performed. It's your responsability to manage false case.
    - *bytes* : The array containing the read bytes 
    - *offset : The index of the channel from which the bytes will be read
    - *length : The number of bytes expected

- **boolean write(bytes[] bytes, int offset, int length);** : Returns true if the write can be performed. It's your responsability to manage false case.
    - *bytes* is a data array that has to be written in the Channel. This function does not ensure *length* bytes will be wrote. If the buffer is empty or a disconnect exception is raised returns the number of read bytes.
    - *offset* : The index of *bytes* from which it should be copied to the Channel
    - *length* : The number of bytes from *offset* that have to be copied
- **void disconnect();** : Stops the connection
- **boolean disconnected();** : Returns true if the channel is disconnected 

## Task
ask objects allows to post runnables on the EventPump

### Attributes

- *private EventPump _pump* The pump on which Runnab les will be posted

- *static Task _currentTask* : The task being run by the pump.

- *private List<Event> _events : The list of all the events posted on this tasks.

- *private boolean _killed : Wheter or not this task has been killed.

## Constructor

- *public Task()* : Sets pump with the su=ingleton insatnce of it and *_killed* to false.

### Methods

- *public void post(Runnable r)*: Creates a event from the *r*, adds it to its list *_events* and post the new event on the pump.
	
- *public void kill()* : _killed = true.

- *public boolean killed()* : returns _killed.

- *public static Task getTask()* : returns static _currentTask.

## EventPump
The EventPump is the one that will execute Runnable posted by tasks. 

**The Pump follows a singleton pattern**

### Attributes

- private *Queue< Runnable > _runnables* : A Queue of all the runnable to execute.

- *private Runnable _currentRunnable* : The Runnable being treated currently.

- *private boolean _running* : a flag to detect if the pump should stop.

### Constructor

- *private EventPump()* : Declares *_runnables* and sets *_runnning* to true

### Method

- *syncronized public post(Runnable runnable)* : Adds A runnable to _runnables. 

- *syncronized public removeRunnable(Runnable runnable)* : Removes A runnable to _runnables. 

- *synchronized private Runnable getNextRunnable(Runnable runnable)* : Returns the next Runnable from *_runnables ands* sets *_currentRunnable*

- *private void run()* : Loops while (_running) on all the runnables. The Ruunable is removed before being treated. If *_runnables* is empty waits until a new runnable is posted.


## Listener should be redefined and suits your specific needs

### AccepteListener
This interface will only be used to define the expected behavior once a connectio is accepted.
It should be redefined for each entity xanted

- *public void accepted(Channel channel)* : The behavior once the connection is accepted with the linked Channel.

### ConnectListener
This interface will only be used to define the expected behavior once a connection is established.
It should be redefined for each entity xanted

- *public void connected(Channel channel)* : The behavior once the connection is established with the linked Channel.

- *public void refused* : Remote broker keeps the right to refuse the connection (and if the asked remote broker doesn't exist)

### Listener
A listener associated to a channel to trigger message callback.

- *void received(byte[] bytes)* : Once all the bytes have been received this is the behavior expected
- *void closed()* : Once the connection is closed
- *void sent(bytes[] message)* 


## BrokerManager Class

BrokerManager is the entity that know all of the brokers and allow them to find eachother.

This class follows a **singleton pattern**.

### Attributes

- *private static final BrokerManager INSTANCE* : The singleton instance. 

- *private HashMap<String, Broker> _brokers* : A map of (name,Broker).

### Constructor

- *public BrokerManager()* initializes the _brokers. 

### Methods

- *public void registerBroker(QueueBroker broker)* : adds *broker* to *_brokers* with its name has a key.

- *public QueueBroker getBroker(String name)* : Returns the Broker with the name *name*. If not found null.