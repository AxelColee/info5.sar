# QueueMessage Specification

## Purpose
An event based message exchanging framework.

## Prerequisite
This framework is full event and won't be threaded safe. Therefore it does not guarantee good results when accessed by multiple threads.

Some objetcs referred here belongs to the EventBased cHannel Framework developped earlier.

## Bootstraping
Bootstapin the app consists of creating all the necessary obejcts (QueueBroker, Task, Listner) and Finally, starting the pump. Only by launching the Eventpump at last will ensure the orrect behavior of the framework.

Furthermore evry method in QueueBroker and QueueMessage should be called in a runnable gthat wille be posted using a task. The framework won't be able to perform correclty if those methods are called directly.

## Binding/Unbinding/connecting
Binding on a broker will accept all the connection on the specified port until an unbind is posted.
In order for a connect to work a bind should already be done on the targetted broker. If a connect and a bind match the connection will be established. Several connect can be done on the same bind.

## Writing/Reading
Write and read actions will be done in the order they have been distributed. It means concureent not linked write acions can be sent and will be write in the same order ensuring data consistency.
Even in this event based implementation, the set will eventually be written without the need to repost write or read actions if only a part is written or read for now.

## Disconnecting
If the local channel disconnects it will reject all of the read and write actions from now on. If actions already accepted are supposed to return a result they will be terminated and surely won't finish. 
If the remote channel is disconnected, read will continue until there is nothing else to read. And write will assume they have been completely wrote, silent dropping all the bytes.

## QueueBroker Class
This class will be used by tasks to initiate communication with other QueueBroker.

### Constructor
*QueueBroker(String name)* : Creates a new QueueuBroker named *name*.

### Methods
*String name()* : Returns the name of the Broker given in the constructor.

*boolean bind(int port, AcceptListener listener)* : Binds a port to this broker accepting all connections until an unvbin and the same port is called. Gives also the listener for once the connection has been established. Returns true is the action can be performed.

*boolean unbind(int port)* : Unbinds a port to this broker. Returns true if it can be performed.

*connect connect(String name, int port, ConnectListener listener)* : Tries to connect to a distant Broker named *name* on the port *port* with the listener for once the connection is established or refused.

## MessageQueue
MessageQueue are used to send and receive messages between connected Brokers.

### Methods 
- *void send(Message msg)* : Sends the message.

- *void close()* : Shuts the connection locally.

- *boolean closed()* : Returns true if the MessageQueue is closed.

## Task
Task allows the user to post runnables which will enventually be executed. They are the accessed point to QueueBroker and queueMesssages methods.

- *public void post(Runnable r)*: Post *r* on the pump 
	
- *public void kill()* : Kills the current task. Once killed, all the runnables posted using this task will be removed and not executed.

- *public boolean killed()* : Returns true if the task is killed

- *public static Task getTask()* : Returns the currently running task.

## Interface AcceptListener

- *void accepted(MessageQueue queue)* : Callback once the connection is accepted. 

## Interface ConnectListener
- *public void connected(MessageQueue queue)* :Callback once connected. 

- *public refused()* : Callback when connection refused.

## Interface MessageListener
This interface is set to a specific MessageQueue adn defines the behavior for message exhanging a MessageQueeuClosing

- *void received(byte[] bytes)* : Callback when the message - *bytes* have been received
void closed() : Callback for when the MessageQueue is closed
- *void sent(Message message)* : Callback for once the message has been sent.