# Design

## Broker Class
Broker has to be **synchronized**. Indeed, when several connections occur on the same broker, each one of them as to be treated sequentially. For instance, it applies for multiple accept et connect coming simultaneously.

To manage all the incomming connections. A broker needs to have a list of Rdv, a class describe later. 


### Attributes

- *private String name* : the name of the broker

- *private _rdvs List< Rdv >* : A list containing the connections on this broker. It means all the incoming connect or a pending accept. This list can contains only one Rdv< Accept >. An IllegalStateException will be raised otherwise. Therefore, the list is pouplated mostly by Rdv< Connect >, Even if they try to connect to the same port. A list element can be removed if a matching accept-connect pair match(this process will be detailed in the Rdv methods).

- *BrokerManager _brokerManager*: A reference to the broker manager. In order to be able to communicate with other brokers.

### Constructor

*Broker(BrokerManager brokerManager, String name)* : Sets *brokerManager* and *name* 

### Methods
- *public Channel accept(int port)* : Declare a port on which other broker will be able to connect. Creates a new RDV instance to be added to rdvs.

- *public Connect(String name, int port)* Tries to connect to the broker *name* on the port *port*. Calls the methods *Connect*
Si le broker manager ne connait pas alors null
If the broker targetted has a running port then waits


- *private Channel Connect(Broker brokerConnect, int port)*: Creates*brokerConnect* tries to connect to find an Rdv < Accept > on the port *port* in *_rdvs*. If not adds a new entry to rdvs. Returns the Communication Channel.

- *private Channel Accept (Broker brokerAccept, int port)* : Tries to find a Rdv< Accept  >  to connect otherwise creates a new Rdv < Connect > instance and adds it to the list. Returns the new communication Channel.


## Rdv Class

### Attributes 

- *private int port* : The port of the Rendez-vous between an accept and a connect. 

- *private Broker brokerAccept, brokerConnect* : The two broker inlvolved in the conection. *brokerAccept* is the broker that called the accept method and *brokerConnect* is trhe one calling Connect method.

- *private channelAccept, channelConnect* : The two channels that will be used by the task to communicate with each other. thos two channles have two circular buffer in common to allow bidirectionnal communication.

### Constructor

*Rdv(int port)* : Sets the port for this rdv


### Methods
-*public Channel connect(Broker brokerConnect)* : Set the distant broker as *BrokerConnect*. Checks if the two broker already arrived. If they haven't, puts the broker thread to sleep. Else wake the other one up and creates a the channele and linked them by two circluarBuffer. The channels have to be created before the Accept thread is woke up. Uses a timeout to stop connection if time exceeds givent time

-*public Channel accept(Broker b)* : Set the local Broker as *brokerAccept*. Checks if the two broker already arrived. If they haven't, puts the broker thread to sleep. Else wake the other one up and creates a the channele and linked them by two circluarBuffer. The channels have to be created before the Connect thread is woke up. 

-*private Channel newChannels()* : Creates two circular buffer referenced them into two channles and set those ones as *channelAccept* and *channelConnect*.

## Task 
Task extends the default class Thread.
Every Task has an set of objects associated in order to achive its function : The first of which is using the runnable given in the constructor (*task.start()*)

### Attributes 

*private Broker _broker* : The broker used by the task to initiate communications with other tasks.

*private Runnable _runnable* : The default behavior of this thread.

### Constructor
*Task(Broker b, Runnnable r)* : Sets the value of *_broker* and *_runnable*

### Methods

- static Broker getBroker();
To call this method, as Task extends Thread , we can access this method evry where using *(Task) Thread.currentThread()*

- *public void start()* : Overwriting the defaukt Thread method and running the class runnable *_runnable*.

## Channel 
Channels are the objects used by the task to communicate. Each Channel is known by a task and knows two circular buffer to communicate withe an other channel.

### Attributes

- *private boolean disconnected* : True if the channels is disconnected. A channel is always connected at creation.

- *private CircularBuffer in* : The CircularBuffer used to read data

- *private CircularBuffer out* : The CircularBuffer used to write data

### Constructor

*Channel(CircularBuffer in, CircularBuffer out)* : Sets the two circularBuffer

### Methods 

*public int write(byte[] bytes, int offset, int length)* : Returns the number of bytes wrote. Calls the method put on the *out* CircularBuffer. if this methods catches an illegalStateException("full"), stop the execution until it can put again. If *disconnected* returns true, a DisconnectedChannelException wille be raised.
If a disconnection channels is received from the other end, write the remaining bytes as if the channles were still connecting, dropping silently the last bytes

*public int read(byte[] bytes, int offset, int length)* : Returns the number of bytes read. Calls the metjod get on the in buffer. if this methods catches an illegalStateException("empty"), stop the execution until it can pull again. If *disconnected* returns true, a DisconnectedChannelException wille be raised.
If a disconnection channels is received from the other end, read the remaining bytes

*disconnect()* : sets *disconnected* to true and sends 

## BrokerManager Class

This class is the entity that knows all of the broker that should be able to access each other. It means a specific broker will use his own attribute brokerMAnaer in order to access and connect to anyother one. 
This class follows the **singleton** pattern
As several broker will access this only entity it has to be **Thread-Safe**

*public Channel Connect(Broker brokerConnect, String name, int port)* : The Broker *brokerConnect tries to establish a connection with the broker *name* on port *port*. Goes trhoug the list of broker and if a name matches calls the method connect on this particular Broker. Returns the channel returned by this broker. Otherwise returns null


## Bootstraping

In order for the different tasks to communicate, a main method has to  create some entities to setup and link all the entities to make it work. 

public static void main(){

    1) Create a Broker
    2) Create a Runnbale
    3) Create a Task using a Broker and a Runnable
    4) Call the method start() on the Task

}

