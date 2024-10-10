### Event QueueMessage

## Prerequisite
This framework should be used event-based wise. Any user shouldn't synchronize any of his listener.

## Purpose
A system allowing several processes to connect to eachother and exchange messages. Although threads will be used.

## Binding, Connecting & Unibinding

If twoi processes want to be able to exchage messages, first they have to initiate connection. 
To do so 3 functions exists : 
 - Binding : A process now waits for a connection on a specified port. I won't stop accepting until The unbind methods is called on the same process.
 - Connect : Connects to a distant process on a specified port when bind is made on the same port. A connect will only be used one.
 - Unbinding : Stops a binding for a specified port, rejecting all connects from now on.

 Not any of the above methods are blocking. There will be a callbac k when they succeeds or fail.

## Send/Receive
Using MessageQueue (see below), users can only send messages. The message won't be sent once the method finished. Instead the message will be given to a lower layer of the framework which will eventually send it.
Receiving is also managed by the framework.

## Behavior Management
As this framework is used as an event based one, all the behaviors after connection (Bind, connnect) and message exhanging (sending and receiveing) will be dictated in three Listeners (described below). 
Therefore a user should implements : 
- AccetptListner to define the expected behavior after a bind succeeds
- ConnectListener to define the expected behavior after a connect succeeds
- Message Listener to define the expected behavior for message srecieved sent or connection closed

Depending on your app, there shouldn't be only one object implementing each of the intrfaces above.

## Class 

### QueueBroker
This class is used to instantiate connection between several processes.

#### Constructor

public QueueBroker(String name) : Defines the name of the new instantiated QueueBroker. That name should be the one used in the connect method (see below).

#### Methods
- *public boolean bind(int port, AcceptListener listener)* : Links a port to the listener which will be used once a connect matches. If two binds uses the same port, it will return false, true otherwise.

- * public boolean unbind(int port)* : Unlink the port to a potential listener. If not any bind uses the *port* it will return true, true otherwise.

- *public boolean connect(String name, int port, ConnnectListener listener)* : Connects to the Broker *name* on *port* and will invoke connected methods of the listener if there is a match. Usually returns true but can return false in some exceptionnal situations. 

### QueueMessage

Queue message is the object that comes after Queue Broker.Meaning it will get be used to send and receive message once broker are conneted.

#### Method

- *void setListener(MessageListener listener)* : Sets the the MessageListener.
- *void send(Message message)* : Sends the message and give the ownership. It's a non blocking method meaning the message will not be sent at the end of this method
- *void close()* : close the connection between QueueMessage.
- *boolean closed()* : Returns true if the connection is closed.

### Message
A class containing a byte array, the offset and length.


### Task
A class used only to start any behavior. For instance, it will post runnable with initial behavior in the bootstraping to connect processes and the in the Listener to send messages. 

#### Methods
- *public abstract void post(Runnable r)* : Post a runnable that will eventually be executed.
- *public static EventTask task()* : Returns the current tasks that is being executed.
- *public abstract void kill()* : Kills this task
- *public abstract boolean killed()* Returns tgrue is this task has been killed.


## Interfaces AcceptListener

- *void accepted(MessageQueue queue)* : Triggers the accepted behavior using *queue*.
Should usually sets the MessageListenr to the Queue

## Interfaces ConnectListener
- *public void connected(MessageQueue queue)* : Triggers the connected behavior using *queue*. 
Should usually sets the MessageListenr to the Queue

- *public refused()* : Triggers the refused behavior.

## Interface MessageListener

This interface describes all the behavior expected for sending message.

- *void received(byte[] bytes)* : Triggers the recieved behavior. *bytes* is the message recieved.
void closed() : Triggers the closed behavior
- *void sent(Message message)* : Triggers the sent behavior. *messsage* is the message sent.