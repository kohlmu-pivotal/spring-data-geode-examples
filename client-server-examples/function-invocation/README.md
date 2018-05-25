# Function Invocation Example

In this example a [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) client will run OQL queries against a `PROXY` and `LOCAL` cache.

TTo the run the examples you require two terminal windows.
 1. deploy and run the stand-alone server as described in the the [Server Configuration](../README.md#server-configuration-and-deployment) of [Client-Server-Examples](../README.md)   
 1. deploy and run the client application as described in the the [Client Configuration](../README.md#client-configuration-and-deployment) of [Client-Server-Examples](../README.md) 


> To run this example make sure you are in the **`$projectRoot/client-server-examples/function-invocation`** directory.

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

Referencing the [FunctionInvocationClient](src/main/java/org/springframework/data/examples/geode/function/client/FunctionInvocationClient.java) or [FunctionInvocationClientKT](src/main/kotlin/org/springframework/data/examples/geode/function/kt/client/FunctionInvocationClientKT.kt)

The example is broken up into multiple steps:
1. Insert (Put) three Customer entries into the `Customers` region using the repositories `save` method.
1.  

After running the client you should see one of the following outputs, depending in client cache type that was run.

1. `PROXY` client cache output 
    ```
    
    ```
1. `LOCAL` client cache output
    ```
    
   ```
   
With a server output of:
   ```
   
   ```