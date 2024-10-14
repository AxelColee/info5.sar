# Event Channel
This is an event based implementation of channels

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Broker Class
The broker is the object that can initiate connection between tasks.
Every broker has to be uniquely identified by a name and be accessed using also a port number.
Even thoug a broker can be used by multiple task instances it will not be synchronized.

### constructor
- **Broker(String name)** :
    - *name* : The broker name which will be used to be connected by a Task.

- **Channel accept(int port);** : Accepts connection on the specified port. If the action is posted returns true.
    If there is already an other accept on the same port, will return false.
    - *port* : the port on which the client tries to connect

- **Channel connect(String name, int port);** Tries connecting to an other broker. If the action is posted, will return true. If the broker doesn't exists or there isn't any accept on *port*, returns false.
    - *name* : The name of the task it tries to connect
    - *port* : The *port* of *name* on which the connection will be established 

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is FIFO and lossless.
Multiple entities writing at the same time won't ensure data consistency.

- **int read(bytes[] bytes, int offset, int length);** : Returns the number of bytes read from the Channel offset. *bytes* will contain all the read bytes in  *bytes*. This function does not ensure *length* bytes will be read. If the buffer is empty or a disconnect exception is raised returns the number of read bytes.
    - *bytes* : The array containing the read bytes 
    - *offset : The index of the channel from which the bytes will be read
    - *length : The number of bytes expected

- **int write(bytes[] bytes, int offset, int length);** : Return the number of bytes wrote in the Channel. 
    - *bytes* is a data array that has to be written in the Channel. This function does not ensure *length* bytes will be wrote. If the buffer is empty or a disconnect exception is raised returns the number of read bytes.
    - *offset* : The index of *bytes* from which it should be copied to the Channel
    - *length* : The number of bytes from *offset* that have to be copied
- **void disconnect();** : Stops the connection
- **boolean disconnected();** : Returns true if the channel is disconnected 

## Task
Task allows the user to post runnables which will enventually be executed.

- *public void post(Runnable r)*: Post *r* on the pump 
	
- *public void kill()* : Kills the current task

- *public boolean killed()* : Returns true if the task is killed

- *public static Task getTask()* : returns the task currently Returning