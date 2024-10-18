# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Binding/Unbinding/connecting
Binding on a broker will accept all the connection on the specified port until an unbind is posted.
In order for a connect to work a bind should already be done on the targetted broker. If a connect and a bind match the connection will be established. Several connect can be done on the same bind.

## Disconnecting
If the local channel disconnects will reject all of the read and write actions. If actions already accepted are supposed to return a result they will be terminated. Meaning, theses actions could have been interrupted and could be false.
If the remote channel disconnects all the write actions will be performed as usual even if they don't reach the remote channel. Else, read will continue as usual.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even though a broker can be used by multiple task instances it **will not be synchronized**.

### constructor
- **Broker(String name)** :
    - *name* : The broker name which will be used to be connected by a Task.

- **boolean Channel bind(int port)** : Binds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **boolean Channel unbind(int port)** : Unbinds connection on the specified port. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **boolean Channel connect(String name, int port);** Tries connecting to an other broker. If the action is posted, will return true. Keeps the right to refuse some bind action. Returns true if performed, false otherwise
    If there is already an other accept on the same port, will return false.
    - *name* : The name of the task it tries to connect
    - *port* : The *port* of *name* on which the connection will be established 

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is **FIFO** and **lossless**.
**Multiple entities writing at the same time won't ensure data consistency.**

- **boolean read(bytes[] bytes, int offset, int length);** : Returns true if the read can be performed. It's your responsability to manage false case.
    - *bytes* : The array containing the read bytes 
    - *offset : The index of the channel from which the bytes will be read
    - *length : The number of bytes expected

- **boolean write(bytes[] bytes, int offset, int length);** : Returns true if the write can be performed. It's your responsability to manage false case.
    - *bytes* is a data array that has to be written in the Channel. This function does not ensure *length* bytes will be wrote. If the buffer is empty or a disconnect exception is raised returns the number of read bytes.
    - *offset* : The index of *bytes* from which it should be copied to the Channel
    - *length* : The number of bytes from *offset* that have to be copied
- **void disconnect();** : Stops the connection
- **boolean disconnected();** : Returns true if the channel is disconnected 

## Task
Task allows the user to post runnables which will enventually be executed.

- *public void post(Runnable r)*: Post *r* on the pump 
	
- *public void kill()* : Kills the current task. Once killed, all the runnables posted using this task will be removed and not executed.

- *public boolean killed()* : Returns true if the task is killed

- *public static Task getTask()* : Returns the currently running task.

## Listener should be redefined and suits your specific needs

### AccepteListener
This interface will only be used to define the expected behavior once a connectio is accepted.
It should be redefined for each entity xanted

- *public void accepted(Channel channel)* : Callback once a connection is accepted.

### ConnectListener
This interface will only be used to define the expected behavior once a connection is established.
It should be redefined for each entity xanted

- *public void connected(Channel channel)* : Callback once connected to the target.

- *public void refused* : Remote broker keeps the right to refuse the connection (and if the asked remote broker doesn't exist).

## WriteListener
- *void written(int bytesWritten)* : Callback for *bytesWritten* bytes wrote. 

### ChannelListener
A listener associated to a channel to trigger message callback.

- *void available()* : Callback once bytes read are available.
- *void disconnected()* : callback for a disconnected channel.
