# Event-Based Channel Framework

## Overview

The `Event-Based Channel` framework provides a non-threaded, event-driven system for facilitating communication between tasks using brokers and channels. The framework enables asynchronous request/response services, allowing tasks to exchange messages in a single-threaded environment.

## Purpose
This framework aims to implement a reliable, FIFO-based, event-driven service, enabling message exchange and task management without the use of threads.

## Broker Class

The `Broker` class manages connections and bindings on specified ports, enabling communication between tasks. Each broker is uniquely identified by a name and associates listeners for handling incoming connections. It operates in a single-threaded environment, so synchronization is not required.

### Attributes

- **private Map<Integer, AcceptListener> _binds**: A map associating port numbers with `AcceptListener` instances to manage incoming connections.
- **private BrokerManager _brokerManager**: The singleton instance of `BrokerManager` that registers and manages brokers.
- **private String _name**: A unique identifier for the broker.

### Constructor

- **public Broker(String name)**: Initializes the broker with a unique name, registers it with `BrokerManager`, and initializes the `_binds` map.

### Methods

- **public boolean unbind(int port)**: Unbinds the specified port, removing any associated listener.
- **public boolean bind(int port, AcceptListener listener)**: Binds a listener to the specified port if not already bound.
- **public boolean connect(String name, int port, ConnectListener listener)**: Attempts to connect to the target broker's port. Returns `true` on success; otherwise, calls `refused()` on the listener.

## Channel Class

The `Channel` class represents a FIFO, lossless communication channel between two connected brokers. Data can be written to and read from the channel, with each channel instance paired to a remote channel for bidirectional communication.

### Attributes

- **private boolean _disconnected**: Indicates if the channel is disconnected. A channel starts connected by default.
- **private boolean _dangling**: Indicates if the remote channel is disconnected. A channel starts connected by default.
- **private Channel _remoteChannel**: The remote channel instance for bidirectional communication.
- **private CircularBuffer _in**: The `CircularBuffer` used for storing incoming data.
- **private CircularBuffer _out**: The `CircularBuffer` used for storing outgoing data.
- **private ChannelListener _listener**: The listener managing channel events (read, write, disconnect).
- **private Queue<byte[]> _writeBuffer**: A queue to buffer outgoing messages, ensuring FIFO order.
- **private Queue<byte[]> _readBuffer**: A queue to buffer incoming messages, ensuring FIFO order.

### Methods

- **boolean read(byte[] bytes)**: Adds a new entry to `_readBuffer` and initiates reading.
- **private void _read(byte[] bytes, int offset, int length)**: Reads as many bytes as possible from `_in`. Reposts events if reading is incomplete.
- **boolean write(byte[] bytes, int offset, int length)**: Adds a new entry to `_writeBuffer` and initiates writing.
- **private void _write(byte[] bytes, int offset, int length)**: Writes as many bytes as possible to `_out`. Reposts events if writing is incomplete.
- **void disconnect()**: Disconnects the channel and calls the disconnect listener.
- **boolean disconnected()**: Returns `true` if the channel is disconnected.
- **void setListener(ChannelListener listener)**: Sets `_listener` to handle channel events.
- **ChannelListener getListener()**: Returns the current `_listener`.

## Task Class

The `Task` class enables posting and managing events within the `EventPump`. Each task tracks its state, including whether it has been terminated, allowing for controlled retries and shutdowns.

### Attributes

- **private EventPump _pump**: The singleton instance of `EventPump` responsible for managing posted events.
- **private List<Runnable> _events**: A list of events associated with the task.
- **private boolean _killed**: Indicates if the task has been terminated.

### Constructor

- **public Task()**: Initializes `_pump` with the singleton instance of `EventPump` and sets `_killed` to `false`.

### Methods

- **public void post(Runnable r)**: Adds the runnable to `_events` and posts it to `_pump` if the task is active.
- **public static Task task()**: Returns the `Task` associated with the current event in the pump.
- **public void kill()**: Marks `_killed` as `true` and removes all associated events from `_pump`.
- **public boolean killed()**: Returns the state of `_killed`.

## EventPump Class

The `EventPump` class is a singleton event dispatcher responsible for managing the event queue and executing events in FIFO order, crucial for the single-threaded nature of the framework.

### Attributes

- **private Queue<Event> _events**: A queue holding events to be processed in FIFO order.
- **private Event _currentEvent**: The event currently being executed.

### Constructor

- **private EventPump()**: Initializes `_events` as an empty queue.

### Methods

- **public void post(Runnable runnable)**: Adds a runnable to `_events`.
- **public void removeEvent(Runnable runnable)**: Removes a runnable from `_events`.
- **private Event getNextEvent()**: Retrieves the next event from `_events` and sets `_currentEvent`.
- **private void start()**: Processes each event in `_events` until the queue is empty.

## CircularBuffer Class

The `CircularBuffer` class is a fixed-capacity, FIFO buffer for storing bytes, enabling efficient push and pull operations.

### Attributes

- **private int m_tail**: Pointer to the start of the buffer.
- **private int m_head**: Pointer to the end of the buffer.
- **private byte[] m_bytes**: Array holding the bytes within the buffer.

### Methods

- **public void push(byte[] data)**: Adds data to the buffer.
- **public byte[] pull()**: Removes data from the buffer in FIFO order.
- **public boolean isFull()**: Checks if the buffer has reached its capacity.
- **public boolean isEmpty()**: Checks if the buffer is empty.

## BrokerManager Class

The `BrokerManager` is a singleton that manages all brokers within the system, allowing for broker registration and retrieval by name.

### Attributes

- **private Map<String, Broker> _brokers**: A map associating broker names with `Broker` instances.

### Methods

- **public Broker register(Broker broker)**: Registers a new broker with a unique name.
- **public void clean()**: Removes all brokers from `_brokers`, clearing the manager.

## Event Class

The `Event` class wraps a runnable and is associated with a specific task, executing the runnable when processed by the `EventPump`.

### Attributes

- **private Task _fromTask**: The task associated with the event.
- **private Runnable _runnable**: The code to be executed when the event runs.

### Methods

- **public void run()**: Executes `_runnable`.

## Listeners

### AcceptListener

The `AcceptListener` interface defines the behavior for accepted connections on a channel. It creates a `Channel` instance upon successful connection.

### ConnectListener

The `ConnectListener` interface manages connection events, including successful connections and connection refusals. It creates a `Channel` upon connection.

### ChannelListener

The `ChannelListener` interface handles data transfer and connection events on a channel:

- **void wrote(byte[] bytes)**: Triggered when data has been successfully written.
- **void read(byte[] bytes)**: Triggered when data has been successfully read.
- **void disconnected()**: Called when the channel is disconnected.
