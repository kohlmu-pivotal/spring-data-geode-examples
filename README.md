Spring Data For Pivotal GemFire and Apache Geode Examples
=========================================================

This project provides a number of examples to get you started using Spring Data for Apache Geode or Pivotal GemFire. These examples are designed to work with [Spring Data for Pivotal GemFire](http://projects.spring.io/spring-data-gemfire) 2.0.7 or higher and are organized into the following sub projects:

* **Client-Server** - These examples pertain to the client-server paradigm. The examples will have a stand-alone server with a client connecting into the server to perform operations. 
* **Embedded** - These examples demonstrate the embedding of a server within an existing application.
* **Advanced** - //TODO

It is important to note that all examples will follow the prescribed Maven directory structure. Also, all examples will be have a Java and [Kotlin](http://kotlinlang.org) implementation. 

# Client Server
These examples are focused on the client-side configuration and functionality, not of the deployed server. The deployed server will be setup with the minimum required configuration to fulfill the requirements of the examples.
Pivotal GemFire / Geode has the ability to configure a client to either be _**look-aside**_ or _**near**_. In some cases the examples will be configured in both ways to show case the capabilities that both provide.

The current examples are:
* **basic-operations** * - In this example the client will perform basic CRUD operations
 
# Embedded
These examples are focused on the configuration of the embedded server and server components.

# Quickstart

These examples show case the application programming model provided by Spring Data for Pivotal GemFire and are not concerned as much with of configuration of Apache Geode or Pivotal GemFire components such as Cache and Region.

The Quickstart examples currently include:

* spring-cache - Using Spring's Cache abstraction with Pivotal GemFire
* repository - Using [Spring Data](http://projects.spring.io/spring-data) Repositories with Pivotal GemFire
* gemfire-template - Using [GemfireTemplate](https://docs.spring.io/spring-data/geode/docs/current/api/org/springframework/data/gemfire/GemfireTemplate.html) to simplify and enhance accessing Region data
* cq - Configuring and using Pivotal GemFire Continuous Queries
* transaction - Demonstrates the use of Pivotal GemFire transactions

# Basic

These examples are focused more on configuring Apache Geode or Pivotal GemFire components
such as Caches and Regions to address various scenarios.

The Basic examples currently include:

* replicated - A simple demonstration of using a REPLICATE Region in a peer-to-peer configuration
* replicated-cs - Similar to the above with a client-server configuration
* partitioned - Demonstrates the use of a PARTITION Region and a custom PartitionResolver
* persistence - Demonstrates the use of persistent backup and disk overflow
* write-through - Demonstrates loading data from and executing synchronous (write-through) or asynchronous (write-behind) updates to a database*
* function - Demonstrates the use of Pivotal GemFire function execution
* java-config - Demonstrates how to configure a Pivotal GemFire Server (data node)
using Spring's Java-based Container Configuration and Spring Data for Pivotal GemFire

# Advanced

These examples demonstrate additional Apache Geode or Pivotal GemFire features
and require a full installation of either Apache Geode or Pivotal GemFire.

You can acquire Apache Geode bits from [here](http://geode.apache.org/releases/).

You can download a trial version of Pivotal GemFire from [here](https://pivotal.io/pivotal-gemfire).

* gateway - Demonstrates how to use and configure a WAN Gateway
* locator-failover - Demonstrates how Pivotal GemFire handles Locator down situations

# Running The Examples

This project is built with Gradle and each example may be run with Gradle or within your Java IDE.
If you are using Eclipse or Spring Tool Suite, go to the directory where you downloaded this project
and type:

        ./gradlew eclipse

If you are using IntelliJ IDEA,

        ./gradlew idea

Detailed instructions for each example may be found in its own README file.

# Running a cache server with custom configuration

As a convenience, this project includes [GenericServer.java](https://github.com/spring-projects/spring-gemfire-examples/blob/master/spring-gemfire-examples-common/src/main/java/org/springframework/data/gemfire/examples/GenericServer.java)
used to start a cache server with a custom Spring configuration. Simply point to a valid Spring configuration on the file system using the built in task:

	./gradlew -q run-generic-server -Pargs=path-to-spring-config-xml-file

This is useful for testing or experimentation with client/server scenarios.
If your application requires additional jars to be deployed to the server, you can create a lib directory under the project root (e.g., spring-gemfire-examples) and drop them in there.
The gradle build is already configured to look there.

Note, this is a 'quick and dirty' way to do this. In a shared integration or production environment, you should use the Pivotal GemFire Shell program, _gfsh_.









