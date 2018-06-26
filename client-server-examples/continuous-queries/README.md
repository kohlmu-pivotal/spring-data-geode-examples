# Basic Client Example

In this example a [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) client will run OQL queries against a `PROXY` and `LOCAL` cache.

TTo the run the examples you require two terminal windows.
 1. deploy and run the stand-alone server as described in the the [Server Configuration](../README.md#server-configuration-and-deployment) of [Client-Server-Examples](../README.md)   
 1. deploy and run the client application as described in the the [Client Configuration](../README.md#client-configuration-and-deployment) of [Client-Server-Examples](../README.md) 


> To run this example make sure you are in the **`$projectRoot/client-server-examples/oql-queries`** directory.

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

Referencing the [OQLClient](src/main/java/org/springframework/data/examples/geode/cq/client/CQClient.java) or [OQLClientKT](src/main/kotlin/examples/springdata/geode/cq/kt/client/consumer/CQConsumerClientKT.kt)

The example is broken up into multiple steps:
1. Insert (Put) three Customer entries into the `Customers` region using the repositories `save` method.
1. Querying the `Customers` region using `CustomerRepository.findById` to find the customer for id=2
1. Querying the `Customers` region using `GemFireTemplate.find` using the OQL query string `select * from /Customers where id=$1` with parameter `2` as an input
1. Updating the customer for id=1 to reflect a name change from `John Smith` to `Jude Smith`
1. Query to find all customers with emailAddress = `3@3.com` using the `CustomerRepository`
1. Query to find all customers with firstName = "Frank" using the `CustomerRepository`
1. Query to find all customers with firstName = "Jude" using the `CustomerRepository`
1. Query to find all customers with firstName = "Jude" using the local client's query service 

After running the client you should see one of the following outputs, depending in client cache type that was run.

1. `PROXY` client cache output 
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Find customer with key=2 using GemFireRepository: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    Find customer with key=2 using GemFireTemplate: [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with emailAddress=3@3.com: [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith), Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with firstName=Frank: [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with firstName=Jude: [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith), Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
    Find customers with firstName=Jude on local client region: []
    ```
1. `LOCAL` client cache output
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Find customer with key=2 using GemFireRepository: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    Find customer with key=2 using GemFireTemplate: [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with emailAddress=3@3.com: [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith), Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with firstName=Frank: [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Find customers with firstName=Jude: [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith), Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
    Find customers with firstName=Jude on local client region: [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith), Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
   ```
   
With a server output of:
   ```
   ... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
   INFO: In region [Customers] created key [1] value [Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)]
   ... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
   INFO: In region [Customers] created key [2] value [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
   ... org.springframework.data.examples.geode.util.LoggingCacheListener afterCreate
   INFO: In region [Customers] created key [3] value [Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
   ... org.springframework.data.examples.geode.util.LoggingCacheListener afterUpdate
   INFO: In region [Customers] updated key [1] [oldValue [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith)]] new value [Customer(id=1, emailAddress=EmailAddress(value=3@3.com), firstName=Jude, lastName=Smith)]
   
   [info 2018/05/23 14:52:44.157 PDT <ServerConnection on port 46015 Thread 0> tid=81] Query Executed in 11.408982 ms; rowCount = 2; indexesUsed(1):emailAddressIndex(Results: 2) "<TRACE> <HINT 'emailAddressIndex'> select * from /Customers customer where customer.emailAddress.value = $1 LIMIT 100"
    
   [info 2018/05/23 14:52:44.166 PDT <ServerConnection on port 46015 Thread 0> tid=81] Query Executed in 0.742146 ms; rowCount = 1; indexesUsed(1):firstNameIndex(Results: 1) "<TRACE> select * from /Customers customer where customer.firstName = $1 LIMIT 100"
    
   [info 2018/05/23 14:52:44.171 PDT <ServerConnection on port 46015 Thread 0> tid=81] Query Executed in 0.605566 ms; rowCount = 2; indexesUsed(1):firstNameIndex(Results: 2) "<TRACE> select * from /Customers customer where customer.firstName = $1 LIMIT 100"
   ```