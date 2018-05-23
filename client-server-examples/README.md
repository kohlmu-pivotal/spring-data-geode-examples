# Client-Server Examples

The examples are aimed at show casing the the power and versatility that a [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) client possess.

All examples will show case the client's ability to either be a `look-aside cache` or `near cache`. The differences in behavior between the two types of caches shall be called out in applicable scenarios.

The examples are broken up into client and server code. Given that the emphasis is on the client-side, the server code is within the `common-client-utilities` module with both JAVA and Kotlin implementations available.

Each example's client code will be available in JAVA and Kotlin as well.
Client's will in most cases be available in two variants:
1. `look-aside cache` - the client stores no data locally
1. `near cache` - the client can store a subset of data locally (depending on available memory)

## Client Configuration and Deployment
The client can be run as either Kotlin or JAVA clients. This itself serves no purpose other than demonstrating that simple and clean interop between Kotlin and JAVA.

The client's themselves can then be run as either `look-aside` or `near`. `look-aside` caches will be better known and referenced as `PROXY` caches and `near` caches will be referenced as `LOCAL` caches.

To run the client examples make ensure you are in the directory of desired example and run the following _**mvn**_ commands

To run a client:
1. Decide on `PROXY` or `LOCAL` implementation
1. Decide on JAVA or Kotlin.

The different options to start a client are:
1. `-DproxyClient` - for a JAVA based `PROXY` cache implementation
1. `-DlocalCacheClient` - for a JAVA based `LOCAL` cache implementation
1. `-DproxyClientKT` - for a Kotlin based `PROXY` cache implementation
1. `-DlocalCacheClientKT` - for a Kotlin based `LOCAL` cache implementation

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
## Server Configuration and Deployment
The server is configured to run on `localhost` and expose port `40404` for the client to connect to.
The server will also create a _Region_ named `Customer` into which the client will write data.

To run the server written in JAVA: 

```
mvn exec:exec -Dserver
``` 

or to run the server written in Kotlin: 

```
mvn exec:exec -DserverKT
```
 
To confirm that the server is running you should see the following lines at the end of the output in the server terminal window: 

```
... CacheServer Configuration:   port=40404 max-connections=800 max-threads=0 notify-by-subscription=true socket-buffer-size=32768 maximum-time-between-pings=60000 maximum-message-count=230000 message-time-to-live=180 eviction-policy=none capacity=1 overflow directory=. groups=[] loadProbe=ConnectionCountProbe loadPollInterval=5000 tcpNoDelay=true
   
... org.springframework.data.examples.geode.basic.kt.server.ServerKTKt logStarted
   INFO: Started ServerKTKt in 6.062 seconds (JVM running for 6.445)
```  
OR 
```
... CacheServer Configuration:   port=40404 max-connections=800 max-threads=0 notify-by-subscription=true socket-buffer-size=32768 maximum-time-between-pings=60000 maximum-message-count=230000 message-time-to-live=180 eviction-policy=none capacity=1 overflow directory=. groups=[] loadProbe=ConnectionCountProbe loadPollInterval=5000 tcpNoDelay=true

.......  Server logStarted
INFO: Started Server in 5.314 seconds (JVM running for 5.694)
```