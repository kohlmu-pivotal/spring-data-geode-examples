Spring Data For Pivotal GemFire and Apache Geode Examples
=========================================================

This project provides a number of examples to get you started using Spring Data for Apache Geode or Pivotal GemFire. These examples are designed to work with [Spring Data for Pivotal GemFire](http://projects.spring.io/spring-data-gemfire) 2.0.9-RELEASE or higher and are organized into the following sub projects:

* **Client Server** - These examples pertain to the client-server paradigm. These examples will involve a client connecting to a server to perform operations. 
* **Server Config** - These examples demonstrate the configuration of servers. This includes regions, persistence, wan, subscriptions, functions and indexes.
* **Advanced** - These examples are to show case how higher-level use cases are solved. e.g caching, write-behind/read-through, transactionality 

It is important to note that all examples will follow the prescribed Maven directory structure. Also, all examples will have both a Java and [Kotlin](http://kotlinlang.org) implementation. 

# Client Server examples
These examples are primarily focused on the client-side configuration and functionality, rather than that of the deployed server. The deployed server will be setup with the minimum required configuration to fulfill the requirements of the examples.

Examples:
* **basic-operations** - In this example the client will perform basic CRUD operations.
* **cluster-defined-regions** - In this example the regions are defined only on the server. The client will use `@EnableClusterConfiguration` to configure regions locally.
* **continuous-queries** - In this example a client puts data into the region on the server, and registers a continuous query on that data.
* **entity-defined-regions** - In this example a server is deployed with no regions defined. The client will use the `@EnableEntityDefinedRegions` to configure regions on the server(s).
* **function-invocation** - In this example the server will have 3 functions registered. The client will invoke each of the functions.
* **oql-queries** - In this example the client will perform OQL queries. This example utilizes both GemFireTemplate and GemFireRepositories to query and implements indexes to increase query performance.
* **security** - In this example the servers and clients are set up with security (username/password) authentication using Geode Security and Apache Shiro.
* **serialization** - In this example the server stores data serialized as a `PdxInstance` instead of using the `Customer` class.
* **transactions** - In this example the client will perform operations within a transaction. First, it will do a successful transaction where entries are saved to the server, and then a failed transaction where all changes are reverted.
 
# Server Config examples
These examples are focused on the configuration of the server and server components.

Examples:
* **async-queues** - In this example the test inserts entries that go through an async event queue on the server which creates OrderProductSummary entries from the data inserted into the other regions.
* **event-handlers** - In this example the server is populated with data, which triggers events in a cache listener, cache writer, and cache loader.
* **region-config** - In this example the server makes use of `PartitionAttributes` to set the number of buckets and redundant copies.
* **wan** - In this example two servers are deploy. One server populates itself with data and the other server gets populated with that data via WAN replication.

# Advanced
These examples will show case higher-level use cases and how Spring Data for Pivotal GemFire simplifies the implementation of the use case.

Examples:
* **cascading-functions** - In this example the client calls a function and feeds the result into another function.

# Running The Examples

Each example has at least one test file located in the test directory. The examples are driven by the tests, so simply run the test either through your IDE or via the commandline. 