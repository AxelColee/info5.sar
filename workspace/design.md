# Design

## Broker
Broker has to be synchronized (especially for accept and connect)

- List< Rdv > : Created by the first one (accept or connect).
Accept + Connect : end RDV
Connect + Accet : end RDV
Accept + Accept : Undetermined
Accept + Accept + Accept : List of RDV

If list >= 2 then only connect

Only one rdv with a connect but there can be multiple rendez vous with an accept with the same port

- BrokerManager brokerManager (static would authorize only one BrokerManager universe)

- *Channel accept(int port)*;
Has to implement a **Rendez-vous**. 


- *Connect(String name, int port)*;
Si le broker manager ne connait pas alors null
If the broker targetted has a running port then waits


- *Channel Connect(Broker, port)*: Serves has the redez vous between tasks in broker

## Rdv

- Port
- Broker : brokerAccept, brokerConect

-*Channel connect(Broker b)* : The distant broker
this.bc = b
if the other one has already arrived create the channel, wake the other one and start
otherwise sleep

-*Channel accept(Broker b)* :
this.bc = c
Same as above

## Task 
Task extends the default class Thread.
Every Task has an set of objects associated in order to achive its function : The first of which is using the runnavle given in the constructor (*task.start()*)

Task(Broker b, Runnnanle r)

- static Broker getBroker();
To call this method, as Task extends Thread , we can access this method evry where using *(Task) Thread.currentThread()*

## Channel 
There has to be 2 channels for evrycommunication
Those two channels knwo the same both circular buffer.
in and out references are rcrossed channel1 in  [    ] out channel2
                                            out [    ] in

- CircularBuffer in (read)
- CircularBuffer out (write)

write{
    put
}

read{
    get
}

disconnect();

## BrokerManager

Lists all the Brokers
Follow a singleton pattern
Has to be thread-Safe

- *getBroker(String name*)


## Bootstraping

public static void main(){

    1) Create a Broker
    2) Create a Runnbale
    3) Create a Task using a Broker and a Runnable

}

