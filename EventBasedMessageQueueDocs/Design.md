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

### Attributes

- *private Broker _broker* : The Broker from eventbasedChannel task

### Constructor
*QueueBroker(String name)* : Creates anew Broker from the name.

### Methods
*String name()* : Returns the name of _broker.

*boolean bind(int port, AcceptListener listener)* : Creates a new Task that posts _broker.bind(port, a custom AcceptListener from the previous task). Therefore different from the Accept listenr given in parameter of this function which is the accept lsitener of this implem.

*boolean unbind(int port)* : Creates a new Task that posts a *_broker.unbind(port)*


*connect connect(String name, int port, ConnectListener listener)* : Creates a new Task that posts _broker.connect(name,port, a custom Connect from the previous task). Therefore different from the Connectlistenr given in parameter of this function which is the accept lsitener of this implem.

## MessageQueue
MessageQueue are used to send and receive messages between connected Brokers.

### Attributes
- *private Channel _channel* : The channel through which it communicates.

### Constructor

- *public MessageQueue(Channel channel)* : Sets _channel.

### Methods 
- *void send(Message msg)* : Creates a new task and post two runnables. The first one is a byte array of the length of the message (on 4 bytes), the second all the wanted bytes from the message.

- *void close()* : Calls _channel.disconnect()

- *boolean closed()* : Returns true if the MessageQueue is closed.

The following interfaces are the custom interfaces from the previous task (EventBasedChannel) not the ones withe the same name in this implem.

## Interface AcceptListener

### Attributes 

- *private AcceptListener _listener* : The accept listener of this implem

### Constructor

- *public AcceptListener(Channel channel)* : Sets _listener.

### Methods

- *void accepted(Channel channel)* : Creates a new Message Queue from *channel* and calls the _listener.accepted(withe the newly created Message Queue).

## Interface ConnectListener
### Attributes 

- *private ConnectListener _listener* : The connect listener of this implem

### Constructor

- *public ConnectListener(Channel channel)* : Sets _listener.

### Methods

- *void connected(Channel channel)* : Creates a new Message Queue from *channel* and calls the _listener.connected(withe the newly created Message Queue).

- *public refused()* : _listener.refused()

## Interface Listener

#### Enum SendingState {Length, Message}
#### Enum ReceivingState {Length, Message}

### Attributes 

- *private MessageListener _listener* : The message listener.
- *private SendingState _sendingState* : A state to define if the current bytes array sent is the length or the actual message.
- *private ReceivingState _receivingState* : A state to define if the current bytes array received is the length or the actual message.

### Constructor

- *public Listener(MessageListener listener)* : sets _listener and both states to send.

- *void read(byte[] bytes)* : As sending message is fifo and by costruction message will have their size sent first. if the state is Length does nothing and changes the satate else calls _listener.received(bytes) and changes the state.


- *void wrote(byte[] bytes)* : As sending message is fifo and by costruction message will have their size sent first. if the state is Length does nothing and changes the state, else calls _listener.sent(bytes) and changes the state.