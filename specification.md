# Event QueueMessage

## Purpose
A system allowing several processes to connect to eachother and exchange messages.

## Overview
A Task is a process, they are the ones thta should communicate with other tasks. A task knows a QueueBroker, to instantiate connection with eachother. Once they do, MessageQueue are returned to tasks. Finally task can call the messageQueues methods send and receive.

## Accepting/Connecting
In order for tasks to be able to connect to each other, a task has to accept connection on a specified port while an other task should connect to the former task broker on the same port. As it is an event based implemention both method used for accepting and connecting aren't blocking.  Instead, the behavoior expected once a task connect or accept will only be run when the a match accept/connect has been find between the two brokers.
Thus making **accept** and **connect** **non-blocking**.

## Send/Receive
Once the connection has been established between two tasks, only then a task can send or receive message from an other one. As we still are is an event-based implementation Send and Receiev won't block the execution. The method will return their messages once it has been delivered or received entirely. Thus making **send** and **receive** **non-blocking**.

Send uses a byte buffer that contains the message to send. Once the method has been called the ownership of this message has been transfered. It means, you don't need to copy the message and keep it before sending it.

## Class

### QueueBroker 

This class is used to instantiate connection between several processes. A Broker can be used by different QueueBroker at the same time.

#### Constructor

*public QueueBroker(String name)* : Defines the name of the new instantiated QueueBroker. That name should be the one used in the connect method (see below)

#### Interfaces AcceptListener

*public interface AcceptListener {
        void accepted(MessageQueue queue);
}* : An interface to define the behavior of a Broker once the a connection has been accepted. It sould be defined in the accepted *accepted* method using the *queue* newly created for the connection between the two tasks. 

*public interface ConnectListener {
    public void connected(MessageQueue queue)
    public refused();
}* : This interface is the listener for connection to a other Broker. If the connection has been established the connected method will be called withe the newly Created queue between the two thasks. Its content is  the expected behavior once the connectionbhas been made. Otherwise, if the connection can't succeed the refused method will be called defining the behavior this kind of situation

#### Method

*public boolean bind(int port, AcceptListener listener)* : Links a port to the listener once a connection has been accepted.

*public boolean unbind(int port):* Unlink the port to a potential listener

*public boolean connect(String name, int port, ConnnectListener listener)* : Tries connecting to the broker named *name* on *port* with the *listener* to use once the connection hs been made.


#### QueueMessage

Queue message is the object that comes after Queue Broker.Meaning it will get be used to send and receive message one broker are conneted. A Queue Message can be used by severl task instances.

##### Interface MessageListener

This interface describes all the behavior expected for sending message.


- *void received(byte[] bytes)* : This method will be called only when all the bytes of the message have been received and are contained in *bytes* and describe what should be done with it.
- *void closed()* : If the messageQueue is closed, describe the expected behavior.
- *void sent(Message message)* : Called when the entire message has been sent and describes the behavior expected after.

#### Method


- *void setListener(Listener listener)* : Sets the listener
- *void send(Message message)* : Sends the message and give the ownership. It's a **non blocking method**
- *void close()* : : close the connection between QueueMessage
- *boolean closed()* : Returns true if the connection is closed.