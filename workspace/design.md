# Design

## Broker Class
Broker has to be **synchronized**. Indeed, when several connections occur on the same broker, each one of them as to be treated sequentially. For instance, it applies for multiple accept et connect coming simultaneously.

To manage all the incomming connections. A broker needs to have a list of Rdv, a class described later. 


### Attributes

- *private String name* : the name of the broker

- *private _rdvs List< Rdv >* : A list containing the connections on this broker. It means all the incoming connect or a pending accept. This list can contains only one Rdv< Accept >. An IllegalStateException will be raised otherwise. Therefore, the list is pouplated mostly by Rdv< Connect >, Even if they try to connect to the same port. A list element can be removed if a matching accept-connect pair match(this process will be detailed in the Rdv methods).

- *BrokerManager _brokerManager*: A reference to the broker manager. In order to be able to communicate with other brokers.

### Constructor

*Broker(BrokerManager brokerManager, String name)* : Sets *brokerManager* and *name* 

### Methods
- *public Channel accept(int port)* : calls private method accept(Broker, port).

- *public Connect(String name, int port)* Tries to connect to the broker *name* on the port *port*. Calls the methods *Connect* on the broker giving its own reference too.

- *private Channel Connect(Broker brokerConnect, int port)*: Creates*brokerConnect* tries to connect to find an Rdv < Accept > on the port *port* in *_rdvs*. If not adds a new entry to rdvs. Returns the Communication Channel. HAs to synchronized *_rdvs* as several threads can access the same broker and so *_rdvs*. if not any rdv match then creates a new rdv and calls connect on this one. 

- *private Channel Accept (Broker brokerAccept, int port)* : Goes through *_rdvs*. If a matching rendez vous is found return accept on this rendez-vous. If an other accept is found raises an IllegalStateException. Finally if the there aren't any match or erro a new Rdv instance of this accept is created 


## Rdv Class

### Attributes 

- *private int port* : The port of the Rendez-vous between an accept and a connect. 

- *private Broker brokerAccept, brokerConnect* : The two broker inlvolved in the conection. *brokerAccept* is the broker that called the accept method and *brokerConnect* is trhe one calling Connect method.

- *private channelAccept, channelConnect* : The two channels that will be used by the task to communicate with each other. thos two channles have two circular buffer in common to allow bidirectionnal communication.

### Constructor

*Rdv(int port)* : Sets the port for this rdv


### Methods
-*public Channel connect(Broker brokerConnect)* : Set the distant broker as *BrokerConnect*. Checks if the two broker already arrived. If they haven't, puts the broker thread waiting. Else notify the other one up and creates a the channel and linked them by two circluarBuffer. The channels have to be created before the Accept thread is woke up. Uses a timeout to stop connection if time exceeds givent time.

-*public Channel accept(Broker brokerAccept)* : Set the local Broker as *brokerAccept*. Checks if the two broker already arrived. If they haven't, puts the broker thread waitig. Else notify the other one up and creates a the channel and linked them by two circluarBuffer. The channels have to be created before the Connect thread is woke up. 

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

- *public void run()* : Overwrites the default Thread method and runs the class runnable *_runnable*.

## Channel 
Channels are the objects used by the task to communicate. Each Channel is known by a task and knows two circular buffer to communicate withe an other channel.

### Attributes

- *private boolean disconnected* : True if the channels is disconnected. A channel is always connected at creation.

- *private CircularBuffer in* : The CircularBuffer used to read data

- *private CircularBuffer out* : The CircularBuffer used to write data

### Constructor

*Channel(CircularBuffer in, CircularBuffer out)* : Sets the two circularBuffer

### Methods 

*public int write(byte[] bytes, int offset, int length)* : Returns the number of bytes wrote. Calls the method put on the *_out* CircularBuffer. if this methods catches an illegalStateException("full"), stop the execution until it can put again (*wait()*). 
Has to synchronize the *_out* buffer. If *disconnected* returns true, a DisconnectedChannelException will be raised.
If a disconnection channels is received from the other end, write the remaining bytes as if the channles were still connecting, dropping silently the last bytes

*public int read(byte[] bytes, int offset, int length)* : Returns the number of bytes read. Calls the metjod get on the in buffer. if this methods catches an illegalStateException("empty"), stop the execution until it can pull again (*wait()*). Has to synchronize on the *_in* buffer. If *disconnected* returns true, a DisconnectedChannelException wille be raised.
If a disconnection channels is received from the other end, read the remaining bytes

*public void disconnect()* : sets *disconnected* to true and sends 

*public boolean disconnected()* : returns *_disconnected*;

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

