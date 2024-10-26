# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even though a broker can be used by multiple task instances it **will not be synchronized**.

### Attributes

- **private Map< Integer, AcceptListener > _binds_** : A map linking the port and the associated AcceptListener.

- **private BrokerManager _broker** : The brokerManager it is associated to.

- **private String _name** : The name of this broker.

- **private BrokerManager _brokerManager** : The borokerManager instance.

### Constructor

- **public Broker(String name)** : Defines the _name. Also sets the unique singleton instance of the BrokerManager and register itself on it and register itself to the brokerManager. Finally initializes the map _accepts.

### Methods

- **public boolean unbind(int port)** : 
If a bind on the port exists, removes it and returns true, else returns false.

- **public abstract boolean bind(int port, AcceptListener listener)** : If a bind on the port doesn't exist, adds one and returns true, else returns false.

- **public boolean connect(String name, int port, ConnectListener listener, Queue Broker)** : If the the broker with the name *name* exists and has the the requested port open, returns true.
Else returns false and called the refused method of the listener.

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is **FIFO** and **lossless**.

## Attributes

- **private boolean _disconnected** : True if the channel is disconnected. A channel is always connected at creation.

- **private boolean _dangling** : True if the remote channel is disconnected. A channel is always connected at creation.

- **private Channel _remoteChannel** : The remote channel.

- **private CircularBuffer _in** : The CircularBuffer used to read data

- **private CircularBuffer _out** : The CircularBuffer used to write data

- **private ChannelListener _listener** : The listener for this channel.

- **private Queue<byte[]> _writeBuffer** : A list of byte array for every message the user wants to send. Used to ensure FIFO messages.

- **private Queue<byte[]> _readBuffer** : A list of byte array for every message the user wants to read. Used to ensure FIFO messages.


- **boolean read(bytes[] bytes)** : Adds a new entry to *_readBuffer*. If *bytes is the only entry of the buffer, calls _read(...).
    - *bytes* : The array containing the read bytes 

- **private void _read(bytes[] bytes, int offset, int length)** : Read as much bytes from _in possible. If the message is not completely read repost event until it is.
Once is it complete calls the listener post event on the next *_readBuffer* entry.
If _in is empty repost the same event.
Regarding dieconnection, it it local terminates all events, if it is distant reads until _in is empty.

- **boolean write(bytes[] bytes, int offset, int length);** : Adds a new entry to *_writeBuffer*. If *bytes is the only entry of the buffer, calls _write_(...).
    - *bytes* : The array containing the bytes to write.

- **private void _write(bytes[] bytes, int offset, int length)** : Write as much bytes on *_out* possible. If the message is not completely write repost event until it is.
Once is it complete calls the listener and posts event on the next *_readBuffer* entry it it exist.
If *_out* is full repost the same event.
Regarding dieconnection, it it local terminates all events, if it is distant calls the listenr on all the entry in *_writeBuffer*

- **void disconnect();** : Stops the connection, and calls the Disconnectlistener.
- **boolean disconnected();** : Returns true if the channel is disconnected 

- **void setListener(ReadListener listener)** : Sets the _listener

- **void getListener()** : returns _listener 

## Task
Task allows the user to post runnables which will enventually be executed.
### Attributes

- **private EventPump _pump** : The pump on which Runnables  will be posted

- **private Runnable _events** : All the events posted using this task

- **private boolean _killed** : Whether or not this task has been killed.

## Constructor

- **public Task()** : Sets pump with the singleton insatnce of it and *_killed* to false.

### Methods

- **public abstract void post(Runnable r)** : creates a new event from the runnable adds it to _events post it on the pump only if is not killed.

- **public static EventTask task()** : Returns the task associated to the currentEvent in the pump.

- **public abstract void kill()** : _killed = true and delets evey event from this task in the pump.

- **public abstract boolean killed()** : Returns _killed

## EventPump
The Pump follows a singleton pattern and will execute runnables in FIFO order.

### Attributes

**private Queue< Event > _events** : A Queue of all the runnable to execute.

**private Runnable _currentEvent** : The Runnable being treated in the start loop.

### Constructor

**private EventPump()** : Declares _runnables.

### Method

**public post(Runnable runnable)** : Adds A runnable to _events_.

**public removeEvent(Runnable runnable)** : Removes A runnable to _runnables.

**private Runnable getNextEvent(Runnable runnable)** : Returns the next Runnable from _runnables and sets _currentEvent

**private void start()** : Loops while *_events* is not empty on all the events and runs them.

## Event Class
Event class wraps the runnable posted on the pump to give it more context regarding tasks that posted it.
**Event Implements Runnable**.

### Attributes

- **private Task _fromTask** : The task which inititated the post
- **private Task _fromTask** : The task which this event haas been posted on.
- **private Runnable _runnable** : The runnable associated with this event

### Constructor
**public Event(Task fromtask, Task mytask, Runnable r)** : sets all the attributes

### Method

- **public void run()** : runs *_runnable*

## Listener should be redefined and suits your specific needs

### AccepteListener
This interface will only be used to define the expected behavior once a connection is accepted.

- **public void accepted(Channel channel)* : Callback once a connection is accepted.

### ConnectListener
This interface will only be used to define the expected behavior once a connection is established.

- **public void connected(Channel channel)** : Callback once connected to the target.

- **public void refused()** : Remote broker keeps the right to refuse the connection (and if the asked remote broker doesn't exist).

## ChannelListener
- **void wrote(byte[] bytes)** : Callback when bytes have been wrote on the channel. 

- **void read(bytes[] )** : Callback once a set of bytes asked has been read. *bytes* is now yours, channels dont keep a copy of it.

- **void disconnected()** : callback for a disconnected channel.