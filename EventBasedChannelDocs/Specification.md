# Event Channel Specification

An event-based implementation of channels for asynchronous communication in a single-threaded, event-driven environment.

## Purpose

The `Event Channel` framework provides a request/response service that allows users to exchange messages efficiently. It is designed for environments where tasks communicate asynchronously through events, eliminating the need for threading. 

## Core Functionalities

### Binding, Unbinding, and Connecting

- **Binding**: Each broker can be bound to a specific port, allowing it to accept incoming connection requests. When bound, a broker listens continuously on the specified port until an unbind operation is performed.
- **Connecting**: A connection attempt can only succeed if the target broker is already bound to the specified port. When a connection request matches an active bind, the framework establishes a communication channel between the brokers. The framework also supports multiple connection requests on the same port, enabling flexible communication patterns.
- **Unbinding**: Unbinding a broker from a port ceases its ability to accept new connections on that port, although active connections continue until they are explicitly disconnected.

### Writing and Reading

The framework enforces a strict FIFO (First-In, First-Out) order for all write and read actions. This ensures that data sent and received maintains consistency, regardless of concurrent actions or the size of the messages.
Once initiated, write and read actions are processed to completion without requiring reposting. This guarantees that even partial data transmission eventually completes, preserving data integrity. A limit on the number of pending write and read actions prevents resource overload and ensures that the system remains responsive. Actions that exceed this limit are rejected until resources are freed.

### Disconnecting

If a local channel disconnects, it automatically rejects all pending and future read and write actions. Previously accepted actions are terminated without guaranteeing a result, preventing further data transmission. In the event of a remote disconnect, the framework completes all pending write actions, though they may not reach the remote destination. Read actions continue until all data is exhausted, ensuring that no data is lost or stranded in the buffer.

## Main Components

### Broker

The `Broker` serves as the core component for managing connections between tasks. Each broker is uniquely identified by a name and can handle multiple connections on specified ports. Operating in a single-threaded, event-driven environment, the broker is not synchronized, and all operations adhere to FIFO ordering.

Brokers use ports to manage bindings and connections. By binding to a port, a broker can accept incoming connections, creating communication channels with clients. Unbinding a port stops the broker from accepting new connections, though active channels remain functional until explicitly disconnected. Brokers can initiate connection requests to other brokers. A successful connection establishes a bidirectional communication channel, enabling message exchanges. Failed connection attempts are handled gracefully, with notifications sent to the requesting entity.

### Channel

A `Channel` is a bidirectional, FIFO, and lossless medium for communication between two brokers. Each channel ensures reliable message transmission, regardless of message size or sequence.

Channels facilitate data transmission through read and write buffers, which store messages in the order they are posted. The FIFO nature of the channel ensures that messages are read and written sequentially. They are designed to be lossless, preserving data integrity by processing messages fully, even if transmission is temporarily interrupted. Channels also monitor the connection state between brokers. When a channel disconnects, it cleans up pending actions and informs both parties, ensuring smooth termination.

### Task

The `Task` component provides a mechanism for managing runnable events in the framework. Each task allows users to post events, which are processed asynchronously by the framework’s event pump. Tasks help in managing and grouping related actions within the event-driven environment.

Tasks can post runnable events to be processed by the framework. This allows for sequential execution without the need for threading. They also support lifecycle management, including the ability to terminate (or “kill”) themselves. When a task is killed, all its pending events are canceled, ensuring that the framework does not process obsolete or unnecessary actions.

### Event Pump

The `Event Pump` is the core dispatcher of the framework, handling all events posted by tasks. As a singleton, it processes events in FIFO order, ensuring predictable and sequential execution within the single-threaded environment.

Events are executed in the order they are posted, ensuring that tasks complete actions consistently and in sequence.
In this case, the event pump can manage events at the task level, including removing all events associated with a specific task when it is killed. This capability helps maintain system responsiveness and prevents the buildup of redundant events.

## Listeners

Listeners in the framework define custom behaviors for various stages of connection and data transfer. They provide flexible handling for connection establishment, data read/write events, and disconnection.

### AcceptListener

The `AcceptListener` handles events triggered when a connection request is accepted. This listener is customizable, allowing users to define specific actions to take once a connection is established. Typically, the listener is responsible for creating a communication channel for the accepted connection.

### ConnectListener

The `ConnectListener` manages the events associated with connection requests. It handles both successful connections and refusals, ensuring that clients are notified of the connection status. When a connection is established, the `ConnectListener` prepares the communication channel for data exchange.

### ChannelListener

The `ChannelListener` is responsible for monitoring data transfer and connection state on an active channel. This includes handling:

- **Data Write Completion**: Signals that a data block has been fully written to the channel, allowing clients to proceed with further operations.
- **Data Read Completion**: Notifies the client when data has been successfully read and is available for processing.
- **Disconnection**: Monitors and responds to disconnection events, informing clients that the channel is no longer active.