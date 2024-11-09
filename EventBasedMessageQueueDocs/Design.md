# QueueMessage Specification

## Purpose
An event-based message exchanging framework.

## Prerequisites
This framework is fully event-driven and not thread-safe. Therefore, it does not guarantee reliable results when accessed by multiple threads.

Some objects referenced here belong to the EventBasedChannel framework developed previously.

## Bootstrapping
Bootstrapping the application consists of creating all the necessary objects (e.g., `QueueBroker`, `Task`, `Listener`) and finally starting the `EventPump`. 
Starting the `EventPump` last ensures the correct behavior of the framework.

Furthermore, every method in `QueueBroker` and `MessageQueue` should be called within a `Runnable` that will be posted using a `Task`. The framework will not perform correctly if these methods are called directly.

## Binding/Unbinding/Connecting
Binding on a broker will accept all incoming connections on the specified port until an unbind request is posted. For a connection to work, a binding must already be active on the target broker. If a `connect` and a `bind` match, the connection will be established. Multiple connections can be made to the same bound port.

## Writing/Reading
Write and read actions will occur in the order they are distributed. This ensures that concurrent, independent write actions will maintain data consistency. Even in this event-based implementation, data will eventually be fully written without needing to repost write or read actions, even if only part of the data is initially written or read.

## Disconnecting
If the local channel disconnects, it will reject all subsequent read and write actions. Actions already accepted but not yet completed will terminate and may not finish. If the remote channel disconnects, `read` operations will continue until there is nothing left to read, while `write` operations will assume the data was fully sent and will silently drop any remaining bytes.

## `QueueBroker` Class
This class is used by tasks to initiate communication with other `QueueBroker` instances.

### Attributes

- *private Broker _broker*: The `Broker` from the EventBasedChannel framework.

### Constructor
- *QueueBroker(String name)*: Creates a new broker with the specified name.

### Methods
- *String name()*: Returns the name of `_broker`.
- *boolean bind(int port, AcceptListener listener)*: Creates a new `Task` that posts `_broker.bind(port, new InternalAcceptListener(listener))`. The internal accept listener is distinct from the `AcceptListener` parameter passed to this function.
- *boolean unbind(int port)*: Creates a new `Task` that posts `_broker.unbind(port)`.
- *boolean connect(String name, int port, ConnectListener listener)*: Creates a new `Task` that posts `_broker.connect(name, port, new InternalConnectListener(listener))`. The internal connect listener is distinct from the `ConnectListener` parameter passed to this function.

## `MessageQueue` Class
`MessageQueue` is used to send and receive messages between connected brokers.

### Attributes
- *private Channel _channel*: The channel through which it communicates.

### Constructor
- *public MessageQueue(Channel channel)*: Sets `_channel` to the specified channel.

### Methods
- *void send(Message msg)*: Creates a new task and posts two `Runnable` actions. The first action sends a 4-byte array representing the length of the message, and the second action sends the message content.
- *void close()*: Calls `_channel.disconnect()`.
- *boolean closed()*: Returns `true` if the `MessageQueue` is closed.

## Interfaces from EventBasedChannel Framework

The following interfaces are custom implementations created in the EventBasedChannel framework, not those with similar names in this framework.

### `AcceptListener` Interface

#### Attributes 
- *private AcceptListener _listener*: The accept listener for this implementation.

#### Constructor
- *public AcceptListener(Channel channel)*: Sets `_listener`.

#### Methods
- *void accepted(Channel channel)*: Creates a new `MessageQueue` from `channel` and calls `_listener.accepted` with the newly created `MessageQueue`.

### `ConnectListener` Interface

#### Attributes 
- *private ConnectListener _listener*: The connect listener for this implementation.

#### Constructor
- *public ConnectListener(Channel channel)*: Sets `_listener`.

#### Methods
- *void connected(Channel channel)*: Creates a new `MessageQueue` from `channel` and calls `_listener.connected` with the newly created `MessageQueue`.
- *void refused()*: Calls `_listener.refused()`. This means that the connection was refused by the remote broker.

## `Listener` Interface

### Enums
- **SendingState**: {Length, Message}
- **ReceivingState**: {Length, Message}

### Attributes 
- *private MessageListener _listener*: The message listener.
- *private SendingState _sendingState*: A state to indicate if the current byte array sent is the length or the actual message.
- *private ReceivingState _receivingState*: A state to indicate if the current byte array received is the length or the actual message.

### Constructor
- *public Listener(MessageListener listener)*: Sets `_listener` and initializes both states for sending.

### Methods
- *void read(byte[] bytes)*: Since message sending is FIFO, messages will have their size sent first. If the state is `Length`, this method does nothing and changes the state; otherwise, it calls `_listener.received(bytes)` and updates the state.
- *void wrote(byte[] bytes)*: Similarly, if the state is `Length`, this method does nothing and changes the state; otherwise, it calls `_listener.sent(bytes)` and updates the state.

