# QueueMessage Specification

## Purpose
An event-based message exchanging framework.

## Prerequisite
This framework is fully event-driven and won't be thread-safe. Therefore, it does not guarantee good results when accessed by multiple threads.

Some objects referred to here belong to the Event-Based Channel Framework developed earlier.

## Bootstrapping
Bootstrapping the app consists of creating all the necessary objects (QueueBroker, Task, Listener) and finally starting the pump. Only by launching the EventPump last will ensure the correct behavior of the framework.

## Binding/Unbinding/Connecting
Binding on a broker will accept all connections on the specified port until an unbind is posted.
In order for a connect to work, a bind should already be done on the targeted broker. If a connect and a bind match, the connection will be established. Several connects can be done on the same bind.

## Writing/Reading
Write and read actions will be done in the order they have been distributed. This means concurrent, not-linked write actions can be sent and will be written in the same order, ensuring data consistency.
Even in this event-based implementation, the set will eventually be written without the need to repost write or read actions if only a part is written or read initially.

## Disconnecting
If the local channel disconnects, it will reject all read and write actions from that point forward. If actions already accepted are supposed to return a result, they will be terminated and likely won't finish. 
If the remote channel is disconnected, read will continue until there is nothing else to read, and write will assume they have been completely written, silently dropping all the bytes.

## QueueBroker Class
This class will be used by tasks to initiate communication with other QueueBrokers.

### Attributes
- *private Broker _broker*: The Broker from Event-Based Channel Task.

### Constructor
*QueueBroker(String name)*: Creates a new Broker from the name.

### Methods
*String name()*: Returns the name of `_broker`.

*boolean bind(int port, AcceptListener listener)*: Creates a new Task that posts `_broker.bind(port, a custom AcceptListener from the previous task)`. This is different from the AcceptListener given in the parameter of this function, which is the accept listener of this implementation.

*boolean unbind(int port)*: Creates a new Task that posts `_broker.unbind(port)`.

*connect connect(String name, int port, ConnectListener listener)*: Creates a new Task that posts `_broker.connect(name, port, a custom Connect from the previous task)`. This is different from the ConnectListener given in the parameter of this function, which is the connect listener of this implementation.

## MessageQueue
MessageQueues are used to send and receive messages between connected Brokers.

### Attributes
- *private Channel _channel*: The channel through which it communicates.

### Constructor
- *public MessageQueue(Channel channel)*: Sets `_channel`.

### Methods 
- *void send(Message msg)*: Creates a new Task and posts two runnables. The first one is a byte array of the length of the message (on 4 bytes), and the second one includes all the desired bytes from the message.

- *void close()*: Calls `_channel.disconnect()`.

- *boolean closed()*: Returns `true` if the MessageQueue is closed.

The following interfaces are the custom interfaces from the previous task (Event-Based Channel) and not the ones with the same name in this implementation.

## Interface AcceptListener

### Attributes 
- *private AcceptListener _listener*: The accept listener of this implementation.

### Constructor
- *public AcceptListener(Channel channel)*: Sets `_listener`.

### Methods
- *void accepted(Channel channel)*: Creates a new MessageQueue from *channel* and calls `_listener.accepted` with the newly created MessageQueue.

## Interface ConnectListener

### Attributes 
- *private ConnectListener _listener*: The connect listener of this implementation.

### Constructor
- *public ConnectListener(Channel channel)*: Sets `_listener`.

### Methods
- *void connected(Channel channel)*: Creates a new MessageQueue from *channel* and calls `_listener.connected` with the newly created MessageQueue.

- *public refused()*: Calls `_listener.refused()`.

## Interface Listener

#### Enum SendingState {Length, Message}
#### Enum ReceivingState {Length, Message}

### Attributes 
- *private MessageListener _listener*: The message listener.
- *private SendingState _sendingState*: A state to define if the current byte array sent is the length or the actual message.
- *private ReceivingState _receivingState*: A state to define if the current byte array received is the length or the actual message.

### Constructor
- *public Listener(MessageListener listener)*: Sets `_listener` and both states to send.

### Methods
- *void read(byte[] bytes)*: As sending messages is FIFO and by construction, messages will have their size sent first. If the state is Length, it does nothing and changes the state; otherwise, it calls `_listener.received(bytes)` and changes the state.

- *void wrote(byte[] bytes)*: As sending messages is FIFO and by construction, messages will have their size sent first. If the state is Length, it does nothing and changes the state; otherwise, it calls `_listener.sent(bytes)` and changes the state.
