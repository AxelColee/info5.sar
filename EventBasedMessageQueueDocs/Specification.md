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

### Constructor
*QueueBroker(String name)*: Creates a new QueueBroker named *name*.

### Methods
*String name()*: Returns the name of the Broker given in the constructor.

*boolean bind(int port, AcceptListener listener)*: Binds a port to this broker, accepting all connections until an unbind on the same port is called. Also provides the listener for once the connection has been established. Returns `true` if the action can be performed.

*boolean unbind(int port)*: Unbinds a port from this broker. Returns `true` if it can be performed.

*connect connect(String name, int port, ConnectListener listener)*: Tries to connect to a distant Broker named *name* on the port *port* with the listener for once the connection is established or refused.

## MessageQueue
MessageQueues are used to send and receive messages between connected Brokers.

### Methods 
- *void send(Message msg)*: Sends the message.

- *void close()*: Shuts the connection locally.

- *boolean closed()*: Returns `true` if the MessageQueue is closed.

## Task
Task allows the user to post runnables which will eventually be executed. They are the access point to QueueBroker and QueueMessage methods.

- *public void post(Runnable r)*: Posts *r* on the pump.
	
- *public void kill()*: Kills the current task. Once killed, all the runnables posted using this task will be removed and not executed.

- *public boolean killed()*: Returns `true` if the task is killed.

- *public static Task getTask()*: Returns the currently running task.

## Interface AcceptListener

- *void accepted(MessageQueue queue)*: Callback once the connection is accepted.

## Interface ConnectListener
- *public void connected(MessageQueue queue)*: Callback once connected.

- *public void refused()*: Callback when connection is refused.

## Interface MessageListener
This interface is set to a specific MessageQueue and defines the behavior for message exchanging and MessageQueue closing.

- *void received(byte[] bytes)*: Callback when the message - *bytes* have been received.

- *void closed()*: Callback for when the MessageQueue is closed.

- *void sent(Message message)*: Callback for once the message has been sent.