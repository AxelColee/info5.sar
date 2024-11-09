# SAR Project Submission Readme

Welcome to the SAR project repository. This document will guide you through setting up, using, and contributing to the project.

## Repository Information

The repository for this project can be found here: [https://github.com/AxelColee/info5.sar/tree/submission](https://github.com/AxelColee/info5.sar/tree/submission). Please make sure to clone the `submission` branch, as it contains the version specifically intended for submission.

The repository includes the fully event-based version of Channel and MessageQueue. At the root of the project, you will find documentation files for each layer (design and specifications). These documents are initial versions that you are encouraged to modify and expand as needed. Some important elements, such as diagrams, may still be missing.

## Eclipse Project Setup

To use this project in Eclipse, import the "workspace" folder. This will ensure that Eclipse properly recognizes the directory structure and sets up the environment correctly.

## Project Structure

The project is organized as follows:

- **Documents**: At the root, you will find the `EventBasedChannelDocs` and `EventBasedMessageQueueDocs` folders, each containing `Design.md` and `Specification.md` files for their respective components.
- **Source Code**: The source code is located in `workspace/coleax/src/info5/sar` and is organized into the following key components:
  - **EventBasedChannel**: Contains the classes responsible for managing channels and events.
    - `Abstract`: Defines key interfaces such as `IAcceptListener`, `IBroker`, `IChannel`, etc.
    - `Impl`: Implements the channel, broker, circular buffer, and event-handling features.
    - `Test`: Contains the `EchoServer` package and `TestChannel.java` for testing the channel functionality.
  - **EventBasedMessageQueue**: Contains classes for managing message queues.
    - `Abstract`: Defines interfaces like `IMessageQueue`, `IQueueBroker`, and others.
    - `Impl`: Implements message queues, queue brokers, and listeners.
    - `Test`: Contains the `EchoServer` package and `TestMessageQueue.java` for testing message queues.
  - **TestMain.java**: This class acts as the main entry point to run all tests for both the EventBasedChannel and EventBasedMessageQueue layers.

The directory structure is illustrated below:

```
.
├── EventBasedChannelDocs
│   ├── Design.md
│   └── Specification.md
├── EventBasedMessageQueueDocs
│   ├── Design.md
│   └── Specification.md
├── README
└── workspace
    └── coleax
        └── src
            ├── info5
            │   └── sar
            │       ├── EventBasedChannel
            │       │   ├── Abstract
            │       │   │   └── (Interfaces)
            │       │   ├── Impl
            │       │   │   └── (Implementations)
            │       │   └── Test
            │       ├── EventBasedMessageQueue
            │       │   ├── Abstract
            │       │   │   └── (Interfaces)
            │       │   ├── Impl
            │       │   │   └── (Implementations)
            │       │   └── Test
            │       └── TestMain.java
```

## Running Tests

The project has a well-defined testing structure. To run the tests, you can either execute them all together or individually for specific layers.

1. **TestMain**: This class runs all tests for both the EventBasedChannel and EventBasedMessageQueue layers.
   - To execute all tests at once, run `TestMain.java`.
  
```
java -cp workspace/coleax/src info5.sar.TestMain
```

2. **Running Individual Test Classes**:
   - Each layer has a **Test** package, which includes individual test classes that can be executed separately:
  - **EventBasedChannel**: This package includes channel-specific tests. 
       - To run channel-specific tests, execute `TestChannel.java`.
```
java -cp workspace/coleax/src info5.sar.EventBasedChannel.Test.TestChannel
```
  - **EventBasedMessageQueue**: This package includes message queue-specific tests.
  - To run message queue-specific tests, execute `TestMessageQueue.java`.
```
java -cp workspace/coleax/src info5.sar.EventBasedMessageQueue.Test.TestMessageQueue
```

The **EchoServer** package within each layer has further sub-packages, `Client` and `Server`, to isolate client and server functionalities. 

## Contribution Guidelines

The base code can still be improved, and contributions are highly encouraged. Please propose your ideas through issues or pull requests and implement them accordingly. However, it is essential that any modifications pass all existing tests to maintain the stability of the project.

If you have suggestions for new features, optimizations, or improvements, please document them clearly before implementing. This helps keep all contributors aligned and ensures the project goals are well understood.

## Documentation

The `EventBasedChannelDocs` and `EventBasedMessageQueueDocs` directories contain the current design and specification documents for each layer. These documents serve as a starting point but may need further enhancements, such as additional diagrams or clarifications. You are encouraged to update these documents as needed.

Thank you for contributing to the SAR project!

## Contributors
Original Repo by: Axel Cole (AxelColee)

Contributors:
- Emin Gundogan (emingundogan)
- Pamella Hani (pamellahani)
- Axel Cole (AxelColee)
- Laure--Anne Bluteau (LaureAnneBluteau)

