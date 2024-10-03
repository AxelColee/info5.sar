# Event Based MessageQueue Design

## Prerequisite
This framework uses both multithreding and events. 

## ThreadedTasks
We will refer as threadedTask when we discuss the Tasks at the top layer of the implenetation. Those tasks have a predeteminded behavior dictated by a Runnable taht is given in the constructor. They extend thread and so will be started() in main. **Each ThreadedTask runs on a Thread**.

ThreadedTasks are the entitites trying to connect and communicate between each other to each other. **But they will only be able to perform Bind and Connect in their Runnable** (the method available in their Broker). How a Task will repond to a connection or bind that succeds will be set in the different listener that, and will be described later(ConnectListener, AcceptListener, MessageListener).

**All in all a Threaded Tasks shouldn't stop its execution at any moment.**


### Attributes

- *private QueueBroker _broker* : The QueueBroker that will initiate connection with other broker and so other ThreadedTasks
- *private Runnable _runnable* : The runnable of this thread

### Constructor

Task(QueueBroker b, Runnable r) : Sets _QueueBroker and _runnnable attributes.

### Methods

Broker getBroker() : Returns _broker.

static Task getTask() : Get the current thread, casts it to Task and returns it.

## Pump
The EventPump is the one that will execute Runnable posted by EventThreads. The runnable that it executes should be short enough to include **one** of the following action :
- send()
- connect()

And above all, they will **not be blocking**.
Meaning every runnable that is posted on the pump should return a result or null. This way the pump can skip to the next runnable it the current can't be dealt with right now.

**A pump will have its own thread** and be be started by the first runnable that's added to its runnables.

**The Pump follows a singleton pattern**

### Attributes

- private *List< Runnable > _runnables* : A List of all the runnable to execute.

### Constructor

- *private EventPump()* : Declares _runnables.

### Method

- *syncronized public addRunnable(Runnable runnable)* : Adds A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list.

- *syncronized public removeRunnable(Runnable runnable)* : Removes A runnable to _runnables. Is **Synchronized** to allow several threads to use the same list.

- *synchronized private Runnable getNextRunnable(Runnable runnable)* : Returns the next Runnable from _runnables.
Is **Synchronized** to allow several threads to use the same list.

- *private void run()* : Loops on all the runnables. If one currentRunnable is killed, it will be directly skipped.

## EventTasks
Event Tasks Shouldn't be visible to a user. They operate between QueueBroker, MessageQueue and the pump. QueueBroker will create new EventTask with a runnbale that tries to connect and be sent on the pump. MessageQueue will create new Event task with a runnable that trie to send.

## Accepting/Connecting


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