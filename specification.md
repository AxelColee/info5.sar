# Event QueueMessage

## Purpose
A system allowing several processes to connect to eachother and exchange messages.

## Overview
A Task is a process, they are the ones thta should communicate with other tasks. A task knows a QueueBroker, to instantiate connection with eachother. ONce they do, MessageQueue are returned to tasks. Finally task can call the messageQueues methods send and receive.

### Class

#### QueueBroker 

This class is used to instantiate connection between several processes. A Broker can be used by different QueueBroker at the same time.

##### Constructor

*public QueueBroker(String name)* : Defines the name of the new instantiated QueueBroker. That name should be the one used in the connect method (see below)

##### Interfaces AcceptListener

*public interface AcceptListener {
        void accepted(MessageQueue queue);
}* : The only method it contains gives a MessageQueue to the Task that has called the broker in first place 

*public interface ConnectListener {
    public void connected(MessageQueue queue)
    public refused();
}* : *connected* method gives back to the task calling a broker the messageQueue associated to the connection

*refused()* method dictates that the connection can't be established

##### Method

*public boolean bind(int port, AcceptListener listener)* : Links a port to a listener in order to notify once the connection has been made.

*public boolean unbind(int port):* Unlink the port to a potential listener

*public boolean connect(String name, int port, ConnnectListener listener)* : Tries connecting to the broker named *name* on *port* with the *listener* to notify once the connection has been established.


#### QueueMessage

##### Interface

*public interface Listener {
        void received(byte[] msg);
        void closed();
    }* 















public abstract class QueueBroker {

    public QueueBroker(String name);

    public interface AcceptListener {
        void accepted(MessageQueue queue);
    }

    public boolean bind(int port, AcceptListener listener);
    public boolean unbind(int port);

    public interface ConnectListener {
        public void connected(MessageQueue queue)
        public refused();
    }

    public boolean connect(String name, int port, ConnnectListener listener;)
}

public abstract class MessageQueue {
    public interface Listener {
        void received(byte[] msg);
        void closed();
    }

    public void setListener(Listener l);

    public boolean send(byte[] bytes);
    public boolean send(byte[] bytes, int offset, int length);

    public void close();
    public boolean closed();
}