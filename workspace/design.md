# QueueMessage Specification

## QueueBroker Class
The Queue BrokerClass uses the Broker implemented in the Channel framework.

## Attributes 
*private Broker _broker* : The Broker from the Channel network that will be used to find nd initiate conncection with other Queue Broker

### Constructor
*QueueBroker(Broke broker)* : Set *_broker* value

### Methods
*String name()* : Returns *_broker* name.

*MessageQueue accept(int port)* : Calls *_broker* accept(int port) method and creates a new MessageQueue from the Channel returned. This method will return The MessageQueue instantiated. 

*MessageQueue connect(String name, int port)* : Calls *_broker* connect(String name, int port) method and creates a new MessageQueue from the Channel returned. This method will return The MessageQueue instantiated. 

## MessageQueue
Message Queue is an overlay of the Channel Class that make sure message, set of bytes, are sent and received.
As seveveral taskss can use it somultaneouly ost of its method needs to be synchronized.

### Methods 
*void send(byte[] bytes, int offset, int length)* : loops over over while each byte hasn't been sent. Moreover, the length of the message sent will be the first byte sent in order for the remote process to know how many bytes it will wait for. This method is synchronized to prevent an other task to send bytes in the middle of the former task message.

*byte[] receive()* : Returns a message as a whole. After the first byte that describes the length of the message, loops over until the whle message is recieved. Finally it returns it. This method is synchronized to prevent an other task to receive bytes in the middle of the former task message.

*void close()* : Calls the disconnect() method offered by the Channel Class

*boolean closed()* : Returns the disconnected() method from the Channel Class

## Task
Task extends thread

## Attributes

*private Broker _broker* : The channel framework Broker
*private QueueBroker _queueBroker* :  The QueueBroker
*private Runnable _runnable : The runnable of this thread

## Constructor

*Task(Broker b, Runnable r)* : Sets *_broker* and *_runnnable*
*Task(QueueBroker b, Runnable r)* : Sets *_QueueBroker* and *_runnnable*

## Methods

*Broker getBroker()* : Returns *_broker* if not null. else Throw IllegalstateException()

*QueueBroker getQueueBroker()* : Returns *_queueBroker* if not null. else Throw IllegalstateException()

*static Task getTask()* : Get the current thread, casts it to Task and returns it.