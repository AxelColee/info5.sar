# EchoServer Specification

## Purpose 
Implement a simple request/response service to allow user to exchange message with one another.

## Broker Class
The broker is the object that can initiate connection between tasks.
As a task can communicate with multiple others, it has to be multithreaded.
Every broker has to be uniquely identified by a name and be accessed using also a port number.

- **Broker(String name)** : Le constructeur du Broker.
    - *name* : The broker name which will be used to be connected by a Task.

- **Channel accept(int port);** : Wait for a client. When one arrives, with the same *port* number, returns the Channel of this communication.
    - *port* : the port on which the client tries to connect

- **Channel connect(String name, int port);** Ask to an other task to connect. This other task respond to the name *name* and the port *port*. Returns this task Channel.
    - *name* : The name of the task it tries to connect
    - *port* : The *port* of *name* on which the connection will be established 

## Channel Class
This bidirectionnal channel is a byte array that can contains data. It can be write or read on. As a prerequisite, this channel is FIFO and lossless.
Although a channel will be multithreaded, multiple entities writing at the same time won't ensure data consistency.

- **int read(bytes[] bytes, int offset, int length);** : Returns the number of bytes read from the Channel offset. *bytes* will contain all the read bytes in  *bytes*. This function does not ensure *length* bytes will be read.
    - *bytes* : The array containing the read bytes 
    - *offset : The index of the channel from which the bytes will be read
    - *length : The number of bytes expected

- **int write(bytes[] bytes, int offset, int length);** : Return the number of bytes wrote in the Channel. 
    - *bytes* is a data array that has to be written in the Channel. This function does not ensure *length* bytes will be wrote.
    - *offset* : The index of *bytes* from which it should be copied to the Channel
    - *length* : The number of bytes from *offset* that have to be copied
- **void disconnect();** : Stops the connection
- **boolean disconnected();** : Returns true if the channel is disconnected 

## Task
Task are the entities that communicate between each other nd can be executed. It uses a Channel to store and recieve information. A task can connect to several Broker.
A Task extends the Thread Class.

- **Task(Broker b, Runnable r)** : Le constructeur de Task.
    - *b* : Le broker de la Task qui permet de se connecter à d'autres Task.
    - *r* : Le Runnable de la Task décrit le comportement spécifique de cette task 

- **static Broker getBroker();** : Returns the broker of this task