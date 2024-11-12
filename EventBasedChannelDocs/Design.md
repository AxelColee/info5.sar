# Event Channel
This is an event-based implementation of channels.

## Purpose 
Implements a simple request/response service to allow users to exchange messages with one another.

## Broker Class
The Broker is the object that can initiate connections between tasks.
Each Broker must be uniquely identified by a name and accessed using a port number.
Even though a Broker can be used by multiple task instances, it **will not be synchronized**.

### Attributes

- **private Map<Integer, AcceptListener> _binds**: A map linking the port and the associated AcceptListener.

- **private BrokerManager _brokerManager**: The BrokerManager instance associated with this broker.

- **private String _name**: The name of this Broker.

### Constructor

- **public Broker(String name)**: Sets `_name`, assigns the unique singleton instance of the BrokerManager, registers itself with the BrokerManager, and initializes the `_binds` map.

### Methods

- **public boolean unbind(int port)**: 
  If a bind on the specified port exists, removes it and returns `true`; otherwise, returns `false`.

- **public abstract boolean bind(int port, AcceptListener listener)**: 
  If a bind on the specified port does not exist, adds one and returns `true`; otherwise, returns `false`.

- **public boolean connect(String name, int port, ConnectListener listener, QueueBroker)**: 
  If the broker with the name *name* exists and has the requested port open, returns `true`; otherwise, returns `false` and calls the listener’s `refused` method.

## Channel Class
This bidirectional channel is a byte array that can contain data. It supports both reading and writing. As a prerequisite, this channel is **FIFO** and **lossless**.

### Attributes

- **private boolean _disconnected**: `true` if the channel is disconnected. Channels are connected by default upon creation.

- **private boolean _dangling**: `true` if the remote channel is disconnected. Channels are connected by default upon creation.

- **private Channel _remoteChannel**: The remote channel.

- **private CircularBuffer _in**: The CircularBuffer used for reading data.

- **private CircularBuffer _out**: The CircularBuffer used for writing data.

- **private ChannelListener _listener**: The listener for this channel.

- **private Queue<byte[]> _writeBuffer**: A queue of byte arrays for each message the user wants to send, ensuring FIFO message ordering.

- **private Queue<byte[]> _readBuffer**: A queue of byte arrays for each message the user wants to read, ensuring FIFO message ordering.

### Methods

- **boolean read(byte[] bytes)**: Adds a new entry to `_readBuffer`. If *bytes* is the only entry in the buffer, calls `_read(...)`.
  - *bytes*: The array containing the read bytes.

- **private void _read(byte[] bytes, int offset, int length)**: Reads as many bytes from `_in` as possible. If the message is incomplete, reposts the event until completion.
  - If `_in` is empty, reposts the same event. In case of disconnection, terminates all events locally; if the disconnection is remote, reads until `_in` is empty.

- **boolean write(byte[] bytes, int offset, int length)**: Adds a new entry to `_writeBuffer`. If *bytes* is the only entry in the buffer, calls `_write(...)`.
  - *bytes*: The array containing the bytes to write.

- **private void _write(byte[] bytes, int offset, int length)**: Writes as many bytes to `_out` as possible. If the message is incomplete, reposts the event until completion.
  - If `_out` is full, reposts the same event. In case of disconnection, terminates all events locally; if the disconnection is remote, notifies the listener for each entry in `_writeBuffer`.

- **void disconnect()**: Stops the connection and calls the DisconnectListener.

- **boolean disconnected()**: Returns `true` if the channel is disconnected.

- **void setListener(ChannelListener listener)**: Sets `_listener`.

- **ChannelListener getListener()**: Returns `_listener`.

## Task Class
Task allows the user to post Runnables that will eventually be executed.

### Attributes

- **private EventPump _pump**: The pump on which Runnables will be posted.

- **private Queue<Runnable> _events**: All events posted using this task.

- **private boolean _killed**: Indicates whether this task has been killed.

### Constructor

- **public Task()**: Sets `_pump` to the singleton instance of the EventPump and `_killed` to `false`.

### Methods

- **public abstract void post(Runnable r)**: Creates a new event from the Runnable, adds it to `_events`, and posts it on the pump if the task is not killed.

- **public static Task getCurrentTask()**: Returns the task associated with the current event in the pump.

- **public abstract void kill()**: Sets `_killed` to `true` and removes all events associated with this task from the pump.

- **public boolean isKilled()**: Returns `_killed`.

## EventPump
The EventPump follows a singleton pattern and executes Runnables in FIFO order.

### Attributes

- **private Queue<Event> _events**: A queue of all the Runnables to execute.

- **private Runnable _currentEvent**: The Runnable currently being executed in the start loop.

### Constructor

- **private EventPump()**: Initializes `_events`.

### Methods

- **public void post(Runnable runnable)**: Adds a Runnable to `_events`.

- **public void removeEvent(Runnable runnable)**: Removes a Runnable from `_events`.

- **private Runnable getNextEvent()**: Retrieves the next Runnable from `_events` and sets `_currentEvent`.

- **private void start()**: Loops through `_events` and executes them while `_events` is not empty.

## Event Class
The Event class wraps the Runnable posted on the pump to give it more context regarding the task that posted it.
**Event implements Runnable**.

### Attributes

- **private Task _fromTask**: The task that initiated the post.

- **private Runnable _runnable**: The Runnable associated with this event.

### Constructor

- **public Event(Task fromTask, Runnable runnable)**: Sets `_fromTask` and `_runnable`.

### Methods

- **public void run()**: Runs `_runnable`.

## Listeners (to be redefined to suit specific needs)

### AcceptListener
Defines the expected behavior once a connection is accepted.

- **public void accepted(Channel channel)**: Callback once a connection is accepted.

### ConnectListener
Defines the expected behavior once a connection is established.

- **public void connected(Channel channel)**: Callback once connected to the target.

- **public void refused()**: Callback if the remote broker refuses the connection or if the requested broker does not exist.

### ChannelListener
Defines the expected behavior for message exchange in a Channel.

- **void wrote(byte[] bytes)**: Callback when bytes have been written to the channel.

- **void read(byte[] bytes)**: Callback once a set of bytes has been read. The channel does not keep a copy of *bytes*.

- **void disconnected()**: Callback for a disconnected channel.
