# QueueMessage Specification

## Purpose
The goal is to develop a framework to send and receive whole messages (a set of bytes). Messsages can be of variable sizes and payload.

The entity communicating, Tasks, are diffferent threads and connect to each other using QueueBroker. Once they do MessageQueue send and receive the message asked by the task.

## QueueBroker Class
This class will be used by tasks to initiate communication with other Broker. Underneath it uses a Broker (developped in task1) to connect only to other QueueBorker.
Moreover, a Broker is always owned by a Task but several tasks can use the same Broker.

### Constructor
*QueueBroker(Broke broker)* : In order to create a queue broker, a Broker *broker* has to be given in the costructor.

### Methods
*String name()* : Returns the name of the Broker given in the constructor.

*MessageQueue accept(int port)* : Set this broker waiting for a connection on the port *port*. There can't several accepts on the same port. Returns a MessageQueue object that will be used to send and receive message by the Tasks. 

*MessageQueue connect(String name, int port)* : Tries to connect to a distant Broker named *name* on the port *port*. If not any broker holds this name, returns null. Else, returns The message queue on which Tasks will be able to communicate.

## MessageQueue
This class dictates how messages are sent and receive.
A messageQueue will be used by Tasks in order to send a receieve whole message (a set of bytes). Message queue will hold a Channel (developped in task1) to communicate.
Furthermore Several task can use the same MessageQueue.

### Methods 
*void send(byte[] bytes, int offset, int length)* : Sends a set of *length* bytes contained in the array *bytes* from *offset* byte. If the connection is closed before the end of the message. This method will keep sending the bytes silently. Send methods can only be executed if the Message Queue is not closed

*byte[] receive()* : Returns a byte Array containing a whole message. If a message is not complete it will be rejected. Send methods can only be executed if the Message Queue is not closed.

*void close()* : Shuts the connection locally and notifies it to the the remote Message Queue.

boolean closed(); Returns true if the MessageQueue is closed.

## Task
A task extends the class Thread. It means every Task will have its own thread and runs simultaneously with other tasks. Task connect to eachother and connects in order to communicate. To do so they can use a standard Broker (see Task1) or use a QueueBroker to send whole messages not just bytes.

## Constructor

*Task(Broker b, Runnable r)* : A task to exist needs to have a Broker to try and connect to other broker for communication and A runnable that dicates the behavior of this task. It uses here a standard broker (developed in task1)
*Task(QueueBroker b, Runnable r)* : A task to exist needs to have a QueueBroker to try and connect to other broker for communication and A runnable that dicates the behavior of this task.

## Methods

*Broker getBroker()* : Returns the Broker associated to this task (given in the constructor). If the constructor using the queue broker has been used to instantiate this task, it throws an illegal state exception.

*QueueBroker getQueueBroker()* : Returns the QueueBroker associated to this task (given in the constructor). If the constructor using the statard broker has been used to instantiate this task, it throws an illegalStateException.

*static Task getTask()* : Returns the current task (Thread) that runs the code. Static acces allow the the  to get the task even without any task reference.