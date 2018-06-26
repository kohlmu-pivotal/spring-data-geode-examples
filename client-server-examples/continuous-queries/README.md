# Continuous Query Client Example

In this example there are will be two [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) 
client caches. The first client cache, the consumer, will register a [continuous query](https://geode.apache.org/docs/guide/16/developing/continuous_querying/chapter_overview.html) 
on a region. The second client cache, the producer, will save 3 entries in the region. The consumer will then print out the CQEvents
that are received from the server cache, that match the provided `SELECT * FROM /Customers` CQQuery.

To the run the examples you require three terminal windows.
 1. deploy and run the stand-alone server as described in the the [Server Configuration](../README.md#server-configuration-and-deployment) of [Client-Server-Examples](../README.md)   
 1. deploy and run the cq consumer client application as described [here](#client-configuration-and-deployment) 
 1. deploy and run the cq producer client application as described [here](#client-configuration-and-deployment) 


> To run this example make sure you are in the **`$projectRoot/client-server-examples/continuous-queries`** directory.

## Client Configuration and Deployment
The client is configured to connect to the deployed/started locator on `localhost` port `10334`.

To start the client you can decided to run one of the following client parameters:
1. `-DproductClient` - for a JAVA based producer implementation
1. `-DconsumerClient` - for a JAVA based CQ consumer implementation
1. `-DproducerClientKT` - for a Kotlin based producer implementation
1. `-DconsumerClientKT` - for a Kotlin based CQ consumer implementation

```
mvn exec:exec -DproductClient
```
```
mvn exec:exec -DconsumerClient
```
```
mvn exec:exec -DproductClientKT
```
```
mvn exec:exec -DconsumerClientKT
```
## Running the example

Referencing the [CQConsumerClient](src/main/java/examples/springdata/geode/cq/client/consumer/CQConsumerClient.java) or 
[CQConsumerClientKT](src/main/kotlin/examples/springdata/geode/cq/kt/client/consumer/CQConsumerClientKT.kt) and
[CQProducerClient](src/main/java/examples/springdata/geode/cq/client/producer/CQProducerClient) or 
[CQProducerClientKT](src/main/kotlin/examples/springdata/geode/cq/kt/client/producer/CQProducerClientKT.kt)

The example is broken up into multiple steps:
1. Insert (Put) three Customer entries into the `Customers` region using the repositories `save` method from the CQProducerClient.
1. The CQConsumerClient has a continuous query `SELECT * FROM /Customers` which should match all entries saved by the CQProducerClient.
The configured handler method `handleEvent` will print the received `CQEvents` to `System.out`. 

After running the examples you should see the following outputs.

1. `CQProducerClient` output:
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Inserted customer = Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)
    Inserted customer = Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)
    Inserted customer = Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)
    ```
1. `CQConsumerClient` output:
    ```
    Received message for CQ 'CustomerJudeCQ'CqEvent [CqName=CustomerJudeCQ; base operation=CREATE; cq operation=CREATE; key=3; value=Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
    Received message for CQ 'CustomerJudeCQ'CqEvent [CqName=CustomerJudeCQ; base operation=CREATE; cq operation=CREATE; key=2; value=Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
    Received message for CQ 'CustomerJudeCQ'CqEvent [CqName=CustomerJudeCQ; base operation=CREATE; cq operation=CREATE; key=1; value=Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)]
    ```
   
With a server output of:
   ```
   ....
   ....
   Press <ENTER> to exit
   [info 2018/06/26 16:18:26.203 PDT <...> tid=0x54] Initializing region _gfe_durable_client_with_id_22_1_queue
   [info 2018/06/26 16:18:26.204 PDT <...> tid=0x54] Initialization of region _gfe_durable_client_with_id_22_1_queue completed
   [info 2018/06/26 16:18:39.955 PDT <...> tid=0x5a] In region [Customers] created key [1] value [Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=John, lastName=Smith)]
   [info 2018/06/26 16:18:39.975 PDT <...> tid=0x5a] In region [Customers] created key [2] value [Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=Frank, lastName=Lamport)]
   [info 2018/06/26 16:18:39.980 PDT <...> tid=0x5a] In region [Customers] created key [3] value [Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Jude, lastName=Simmons)]
   ```