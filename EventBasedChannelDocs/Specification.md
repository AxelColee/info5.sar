# Event Channel
This is an event-based implementation of channels.

## Purpose 
Implement a simple request/response service to allow users to exchange messages with one another.

## Binding/Unbinding/Connecting
Binding on a broker will accept all connections on the specified port until an unbind is posted.  
In order for a connect to work, a bind should already be done on the targeted broker. If a connect and a bind match, the connection will be established. Several connects can be done on the same bind.

## Writing/Reading
Write and read actions will be done in the order they have been distributed. This means concurrent, unlinked write actions can be sent and will be written in the same order, ensuring data consistency.  
Even in this event-based implementation, the set will eventually be written without the need to repost write or read actions.  
Moreover, a limit is set, preventing the user from posting too many write or read actions.

## Disconnecting
If the local channel disconnects, it will reject all read and write actions. If actions already accepted are supposed to return a result, they will be terminated without properly finishing.  
If the remote channel disconnects, all write actions will be performed as usual, even if they don't reach the remote channel. Read actions will continue until there is nothing left.

## Broker Class
The broker is the object that can initiate connections between tasks.  
Every broker has to be uniquely identified by a name and accessed using a port number.  
Even though a broker can be used by multiple task instances, it **will not be synchronized**.

### Constructor
- **Broker(String name)**:
    - *name*: The broker name used to connect.

- **boolean Channel bind(String name, AcceptListener listener)**: Binds a connection on the specified port. It reserves the right to refuse some bind actions. Returns `true` if performed, `false` otherwise.  
    If there is already another accept on the same port, it will return `false`.
    - *port*: The port on which the client tries to connect.
    - *listener*: The listener called once the connection is accepted.

- **boolean Channel unbind(int port)**: Unbinds the connection on the specified port. It reserves the right to refuse some unbind actions. Returns `true` if performed, `false` otherwise.  
    If there is already another accept on the same port, it will return `false`.
    - *port*: The port on which the client tries to connect.

- **boolean Channel connect(String name, int port, ConnectListener listener)**: Attempts to connect to another broker. If the action is posted, it will return `true`. It reserves the right to refuse some connect actions. Returns `true` if performed, `false` otherwise.  
    If there is already another accept on the same port, it will return `false`.
    - *name*: The name of the broker it tries to connect to.
    - *port*: The *port* of *name* on which the connection will be established.
    - *listener*: The listener called once connected or if the connection is refused.

## Channel Class
This bidirectional channel is a byte array that can contain data. It can be written to or read from.  
As a prerequisite, this channel is **FIFO** and **lossless**.

- **boolean read(byte[] bytes)**: Returns `true` if the read is posted or performed, `false` otherwise. If the read exceeds a limit, it will not be posted and will return `false`.
    - *bytes*: The byte array to read from.

- **boolean write(byte[] bytes, int offset, int length)**: Returns `true` if the write can be performed, `false` if the write exceeds a limit.
    - *bytes*: The data array to be written in the channel.

- **void disconnect()**: Stops the connection.

- **boolean disconnected()**: Returns `true` if the channel is disconnected.

- **void setListener(ChannelListener listener)**: Sets the read listener.

- **ChannelListener getListener()**: Returns the listener.

## Task
Task allows the user to post runnables which will eventually be executed.

- **public void post(Runnable r)**: Posts *r* on the pump.

- **public void kill()**: Kills the current task. Once killed, all the runnables posted using this task will be removed and not executed.

- **public boolean killed()**: Returns `true` if the task is killed.

- **public static Task getTask()**: Returns the currently running task.

## Listeners (should be redefined to suit specific needs)

### AcceptListener
This interface is used to define the expected behavior once a connection is accepted.  
It should be redefined for each desired entity.

- **public void accepted(Channel channel)**: Callback once a connection is accepted.

### ConnectListener
This interface is used to define the expected behavior once a connection is established.  
It should be redefined for each desired entity.

- **public void connected(Channel channel)**: Callback once connected to the target.

- **public void refused()**: Callback when the remote broker refuses the connection (or if the requested remote broker doesn't exist).

## ChannelListener
- **void wrote(byte[] bytes)**: Callback when all the bytes have been written on the channel.

- **void read(byte[] bytes)**: Callback once a set of bytes has been read. *bytes* is now owned by the user; channels don’t keep a copy of it.

- **void disconnected()**: Callback for a disconnected channel.
