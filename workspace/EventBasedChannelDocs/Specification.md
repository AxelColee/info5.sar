# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Binding/Unbinding/connecting
Binding on a broker will accept all the connection on the specified port until an unbind is posted.
In order for a connect to work a bind should already be done on the targetted broker. If a connect and a bind match the connection will be established. Several connect can be done on the same bind.

## Writing/Reading
Write and read actions will be done in the order they have been distributed. It means concureent not linked write acions can be sent and will be write in the same order ensuring data consistency.
Even in this event based implementation, the set will eventually be written without the need to repost write or read actions.
Moreover a limit is fixed preventing the user from posting too much write or read action.

## Disconnecting
If the local channel disconnects, it will reject all of the read and write actions. If actions already accepted are supposed to return a result they will be terminated without properly finishing.
If the remote channel disconnects all the write actions will be performed as usual even if they don't reach the remote channel. Else, read will continue until there is nothing left.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even though a broker can be used by multiple task instances it **will not be synchronized**.

### constructor
- **Broker(String name)** :
    - *name* : The broker name used to connect.

- **boolean Channel bind(String name, AcceptListener listener)** : Binds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect
    - *listener* : The listener called once the the conection is accepted.

- **boolean Channel unbind(int port)** : Unbinds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **boolean Channel connect(String name, int port, ConnectListener listener);** Tries connecting to an other broker. If the action is posted, will return true. Keeps the right to refuse some connect action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *name* : The name of the broker it tries to connect
    - *port* : The *port* of *name* on which the connection will be established 
    - *listener* : The listener called once connected or if the connectio is refused.

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is **FIFO** and **lossless**.

- **boolean read(bytes[] bytes);** : Returns true if the read is posted or performed. false otherwise. If read exceeds a limit, the will not be posted and return false.
    - *bytes* : the byte array to read on.

- **boolean write(bytes[] bytes, int offset, int length);** : Returns true if the write can be performed, false if write exceeds a limit.
    - *bytes* is a data array that has to be written in the Channel.

- **void disconnect();** : Stops the connection
- **boolean disconnected()** : Returns true if the channel is disconnected 

- **void setListener(ChannelListener listener)** : Sets the read Listener

- **void getListener()** : Returns the listener.


## Task
Task allows the user to post runnables which will enventually be executed.

- **public void post(Runnable r)**: Post *r* on the pump 
	
- **public void kill()** : Kills the current task. Once killed, all the runnables posted using this task will be removed and not executed.

- **public boolean killed()** : Returns true if the task is killed

- **public static Task getTask()** : Returns the currently running task.

## Listener should be redefined and suits your specific needs

### AccepteListener
This interface will only be used to define the expected behavior once a connectio is accepted.
It should be redefined for each entity wanted

- **public void accepted(Channel channel)** : Callback once a connection is accepted.

### ConnectListener
This interface will only be used to define the expected behavior once a connection is established.
It should be redefined for each entity xanted

- **public void connected(Channel channel)** : Callback once connected to the target.

- **public void refused** : Remote broker keeps the right to refuse the connection (and if the asked remote broker doesn't exist).

## ChannelListener
- **void wrote(byte[] bytes)** : Callback when all the bytes have been wrote on the channel. 

- **void read(bytes[] )** : Callback once a set of bytes asked has been read. *bytes* is now yours, channels dont keep a copy of it.

- **void disconnected()** : callback for a disconnected channel.
