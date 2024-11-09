# QueueMessage Specification

## Overview

`QueueMessage` is an event-driven message exchange framework designed for asynchronous, single-threaded communication between brokers. The framework relies on tasks and event-based message handling, ensuring sequential consistency and smooth data flow without using threads.

## Prerequisites

- **Single-threaded Environment**: `QueueMessage` is fully event-driven and is not thread-safe. Access by multiple threads can lead to unpredictable behavior.
- **Dependency on EventBasedChannel Framework**: This framework builds upon components from the previously developed `EventBasedChannel` framework.

## Bootstrapping

The framework requires setting up objects such as `QueueBroker`, `Task`, and `Listener` instances before starting the `EventPump`. Only when the `EventPump` is started last will the framework operate correctly, ensuring all components are prepared to handle events.

Additionally, calls to functions in `QueueBroker` and `MessageQueue` should be wrapped within `Runnable` tasks and posted via `Task` instances. Direct invocation of these methods without task posting may disrupt the framework’s expected behavior.

## Key Functionalities

### Binding, Unbinding, and Connecting

- **Binding**: Brokers can bind to a specified port, enabling them to accept incoming connections. The port remains active for new connections until an unbind request is posted.
- **Connecting**: For a connection attempt to succeed, the target broker must have an active binding on the specified port. Multiple clients can connect to the same bound port.
- **Unbinding**: Unbinding a port on a broker removes its availability for incoming connections.

### Writing and Reading

`QueueMessage` maintains the order of write and read actions as they are distributed. This ordered consistency ensures data integrity across independent actions. The framework will fully process data once a write or read action is initiated, without requiring reposting, even if data delivery is partial at first.

### Disconnecting

- **Local Channel Disconnect**: When a local channel disconnects, all subsequent read and write actions are rejected, and any pending actions may be interrupted without completion.
- **Remote Channel Disconnect**: If the remote end disconnects, read operations continue until the queue is empty, and write operations will act as if data was fully sent, silently ignoring any remaining bytes.

## Core Components

### QueueBroker

The `QueueBroker` class manages communication channels and connections between brokers, supporting connection establishment and message flow across brokers. A `QueueBroker` can handle multiple connections and ports, allowing flexible interactions within the framework.

### MessageQueue

`MessageQueue` facilitates message sending and receiving between brokers. Each queue instance is tied to a specific communication channel and ensures that message exchanges are handled in an ordered, consistent manner. A `MessageQueue` instance also manages the state of the connection, tracking whether it is active or closed.

## Interfaces

### AcceptListener

`AcceptListener` is responsible for handling accepted connections from brokers, creating a new `MessageQueue` instance upon acceptance. This custom interface, originating from the EventBasedChannel framework, allows the system to dynamically manage client connections as they are accepted.

### ConnectListener

The `ConnectListener` interface manages client connection events, including successful connections and connection refusals. This component creates a `MessageQueue` when a connection is established, managing the lifecycle of connections in the event-driven framework.

### Listener

`Listener` monitors messages sent and received within the framework. It uses internal states for message length and content to manage each stage of the message lifecycle, ensuring consistent data handling across communication events.
