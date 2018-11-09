# Basic Client Example

In this example a [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) client will be configured to perform the basic CRUD operations.

To the run the examples you require two terminal windows.
1. deploy and run the stand-alone server as described in the the [Server Configuration](../README.md#server-configuration-and-deployment) of [Client-Server-Examples](../README.md)   
1. deploy and run the client application as described in the the [Client Configuration](../README.md#client-configuration-and-deployment) of [Client-Server-Examples](../README.md) 

> To run this example make sure you are in the **`$projectRoot/client-server-examples/basic-operations`** directory.

## Client Configuration and Deployment
The client is configured to connect to the deployed/started server on `localhost` port `40404`.

To start the client you can decided to run one of the following client parameters:
1. `-DproxyClient` - for a JAVA based `look-aside cache` implementation
1. `-DlocalCacheClient` - for a JAVA based `near cache` implementation
1. `-DproxyClientKT` - for a Kotlin based `look-aside cache` implementation
1. `-DlocalCacheClientKT` - for a Kotlin based `near cache` implementation

```
mvn exec:exec -DproxyClient
```
```
mvn exec:exec -DlocalCacheClient
```
```
mvn exec:exec -DproxyClientKT
```
```
mvn exec:exec -DlocalCacheClientKT
```
## Running the example
Referencing the [BasicClient](src/main/java/org/springframework/data/examples/geode/basic/client/BasicClient.java) or [BasicClientKT](src/main/java/org/springframework/data/examples/geode/basic/kt/client/BasicClientKT.kt)


The example is broken up into multiple steps:
1. Insert (Put) three Customer entries into the `Customers` region using the repositories `save` method.
1. Printing out the size of the local region on the cache
1. Printing out the size of the region on the server
1. Updating the Customer for id=2. Recording the before and after Customer detail
1. Removing (delete) the Customer for id=3 

After running the client has one should see one of the following outputs. The most notable differences between the two outputs is the line `Entries on Client:...`. 

In the `PROXY` cache example, the client does not store any data locally on the client and will return `0` for the number of `Entries on Client`.
The `LOCAL` cache example, the client will store the entries locally, thereby the `Entries on Client` will be `3`.

1. `PROXY` cache client output
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Entries on Client: 0
    Entries on Server: 3
    	 Entry: 
     		 Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)
    	 Entry: 
     		 Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    	 Entry: 
     		 Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)
    Updating entry for key: 2
    Entry Before: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    Entry After: Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)
    Removing entry for key: 3
    Entries:
    	 Entry: 
     		 Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)
    	 Entry: 
     		 Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)

    ```
1. `LOCAL` cache client output
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Entries on Client: 3
    Entries on Server: 3
    	 Entry: 
     		 Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)
    	 Entry: 
     		 Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    	 Entry: 
     		 Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)
    Updating entry for key: 2
    Entry Before: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    Entry After: Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)
    Removing entry for key: 3
    Entries:
    	 Entry: 
     		 Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)
    	 Entry: 
     		 Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)

    ```
Correspondingly you should see the following output in the server terminal.
```
... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
INFO: In region [Customers] created key [1] value [Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)]
... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
INFO: In region [Customers] created key [2] value [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
INFO: In region [Customers] created key [3] value [Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
org.springframework.data.examples.geode.util.LoggingCacheListener afterUpdate
INFO: In region [Customers] updated key [2] [oldValue [Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)]] 
  new value [Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=Sam, lastName=Spacey)]
... org.springframework.data.examples.geode.util.LoggingCacheListener afterDestroy
INFO: In region [Customers] destroyed key [3] 
```