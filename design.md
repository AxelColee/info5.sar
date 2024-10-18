# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even though a broker can be used by multiple task instances it **will not be synchronized**.

### Attributes

- *private Map< Integer, AcceptListener > _binds_* : A map linking the port and the associated AcceptListener.

- *private BrokerManager _broker* : The brokerManager it is associated to.

- *private String _name* : The name of this broker.

- *private BrokerManager _brokerManager : The borokerManager instance.

### Constructor

*public Broker(String name)* : Defines the _name. Also sets the unique singleton instance of the BrokerManager and register itself on it and register itself to the brokerManager. Finally initializes the map _accepts.

### Methods

- *public boolean unbind(int port)* : Creates a new task with a runnable calling _unbind(port, this). Returns true if the action is posted, false otherwise

- *private _unbind(int port, Queue broker broker)* : 
 removes the entry *port* of *_accepts*.

- *public abstract boolean bind(int port, AcceptListener listener)* : Creates a new task with a runnable that adds a new entry *port*,*listener* on *_accepts* of *broker*. Returns true if the action is posted, false otherwise

- *private boolean _bind(int port, AcceptListener listener)* : Adds a new entry on _binds if it doesn't already exists.

- *public boolean connect(String name, int port, ConnectListener listener, Queue Broker)* : Creates a new EventTask containing a Runnable that tries connecting to the broker *name* on *port* and sends *listener* with it. 

- *private boolean _connect(int port, ConnectListener listner)* : If a match is found  creates new Channel, calls listner.connected(queue1), currentAcceptlistener.Accepted(queue2) with the correct queue and returns true. If not match have been found, returns false.

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is **FIFO** and **lossless**.
**Multiple entities writing at the same time won't ensure data consistency.**

## Attributes

- *private boolean _disconnected* : True if the channel is disconnected. A channel is always connected at creation.

- *private boolean _dangling* : True if the remote channel is disconnected. A channel is always connected at creation.

- *private Channel _remoteChannel* : The remote channel.

- *private CircularBuffer _in* : The CircularBuffer used to read data

- *private CircularBuffer _out* : The CircularBuffer used to write data

- *private Queue<byte[]> _writeBuffer* : A list of byte array for every message the user wants to send. Used to ensure FIFO messages.

- *private Queue<byte[]> _readBuffer* : A list of byte array for every message the user wants to read. Used to ensure FIFO messages.


- **boolean read(bytes[] bytes, int offset, int length);** : Adds a new entry to *_readBuffer*. If list is empty post the first event with _read.
    - *bytes* : The array containing the read bytes 
    - *offset : The index of the channel from which the bytes will be read
    - *length : The number of bytes expected

- **private int _read(bytes[] bytes, int offset, int length)** : calls channel in read. If message completely read calls the listener and if list empty posts a new event for the next . else repost an event of the same method but with the right offset value (from channel read). Repost also if if _in is empty

- **boolean write(bytes[] bytes, int offset, int length);** :Adds a new entry to *_writeBuffer*. If list is empty post the first event.
    - *bytes* is a data array that has to be written in the Channel. This function does not ensure *length* bytes will be wrote. If the buffer is empty or a disconnect exception is raised returns the number of read bytes.
    - *offset* : The index of *bytes* from which it should be copied to the Channel
    - *length* : The number of bytes from *offset* that have to be copied

- **private int _write(bytes[] bytes, int offset, int length)** : calls channels write. If message completely read calls the listener and if list not empty post a new event. else repost an event of the same method but with the right offset value (from channel write). Repost also if if _out is full.

- **void disconnect();** : Stops the connection, and calls the Disconnectlistener.
- **boolean disconnected();** : Returns true if the channel is disconnected 

- **void setReadListener(ReadListener listener)** : Sets the read Listeenr
- **void setDsiconnectListener(ReadDisconnnect listener)** : Sets the read Listeenr

## Task
Task allows the user to post runnables which will enventually be executed.
### Attributes

- *private EventPump _pump* The pump on which Runnab les will be posted

- *private Runnable _events* : All the events posted using this task

- *private boolean _killed : Wheter or not this task has been killed.

## Constructor

- *public Task()* : Sets pump with the su=ingleton insatnce of it and *_killed* to false.

### Methods

- *public abstract void post(Runnable r) : creates a new event from the runnable adds it to _events post it on the pump only if is not killed.

- *public static EventTask task()* : Returns the task associated to the currentEvent in the pump.

- *public abstract void kill()* : _killed = true and delets evey event from this task in the pump.

- *public abstract boolean killed()* : Returns _killed

## EventPump
The Pump follows a singleton pattern and will execute runnables in FIFO order.

### Attributes

    private Queue< Event > _events : A Queue of all the runnable to execute.

    private Runnable _currentEvent : The Runnable being treated in the start loop.

### Constructor

    private EventPump() : Declares _runnables.

### Method

    syncronized public post(Runnable runnable) : Adds A runnable to _runnables. Is Synchronized to allow several threads to use the same list. Notifies when finishes.

    syncronized public removeRunnable(Runnable runnable) : Removes A runnable to _runnables. Is Synchronized to allow several threads to use the same list.

    synchronized private Runnable getNextRunnable(Runnable runnable) : Returns the next Runnable from _runnables. Is Synchronized to allow several threads to use the same list.

    private void start() : Loops while (_running) on all the runnables. If one currentRunnable is killed, it will be directly skipped. The Ruunable is removed before of _runnables before being tested. If a Runnable isn't killed at the end of its run function then it will be posted again.

## Event Class
Evnt class wraps the runnable posted on the pump to give it more context regarding tasks that posted it.
Event Implements Runnable.

### Attributes

- **private Task _fromTask** : The task which inititated the post
- **private Task _fromTask** : The task i which this event haas been posted
- **private Runnable _runnable** : The runnable associated with this event

### Constructor
**public Event(Task fromtask, Task mytask, Runnable r)** : sets all the attributes

### Method

- **public void run()** : runs *_runnable*

## Listener should be redefined and suits your specific needs

### AccepteListener
This interface will only be used to define the expected behavior once a connectio is accepted.
It should be redefined for each entity wanted

- *public void accepted(Channel channel)* : Callback once a connection is accepted.

### ConnectListener
This interface will only be used to define the expected behavior once a connection is established.
It should be redefined for each entity xanted

- *public void connected(Channel channel)* : Callback once connected to the target.

- *public void refused* : Remote broker keeps the right to refuse the connection (and if the asked remote broker doesn't exist).

## WriteListener
- *void wrote(byte[] bytes)* : Callback when bytes have been wrote on the channel. 

### ReadlListener
A listener associated to a channel to trigger message callback.

- *void read(bytes[] )* : Callback once a set of bytes asked has been read. *bytes* is now yours, channels dont keep a copy of it.

## DisconnectListener
- *void disconnected()* : callback for a disconnected channel.