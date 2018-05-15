BasicClient Example
===================

In this example a [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) client will be configured to perform the basic CRUD operations.

To the run the examples you require two terminal windows.
1) deploy and run the stand-alone server
2) deploy and run the client application

> To run this example make sure you are in the `$projectRoot/client-server-examples/basic-operations` directory.

#### Server Configuration and Deployment
The server is configured to run on `localhost` and expose port `40404` for the client to connect to.
The server will also create a _Replicate Region_ named `Customer` in which the client will write data.

To run the server written in JAVA: 

```
mvn exec:exec -Dserver
``` 

or to run the server written in Kotlin: 

```
mvn exec:exec -DserverKT
```
 
To confirm that the server is running you should see the following: 

```
....... CacheServer Configuration:   port=40404 max-connections=800 max-threads=0 notify-by-subscription=true socket-buffer-size=32768 maximum-time-between-pings=60000 maximum-message-count=230000 message-time-to-live=180 eviction-policy=none capacity=1 overflow directory=. groups=[] loadProbe=ConnectionCountProbe loadPollInterval=5000 tcpNoDelay=true
   
....... org.springframework.data.examples.geode.basic.kt.server.ServerKTKt logStarted
   INFO: Started ServerKTKt in 6.062 seconds (JVM running for 6.445)
```  
OR 
```
....... CacheServer Configuration:   port=40404 max-connections=800 max-threads=0 notify-by-subscription=true socket-buffer-size=32768 maximum-time-between-pings=60000 maximum-message-count=230000 message-time-to-live=180 eviction-policy=none capacity=1 overflow directory=. groups=[] loadProbe=ConnectionCountProbe loadPollInterval=5000 tcpNoDelay=true

.......  org.springframework.data.examples.geode.basic.server.Server logStarted
INFO: Started Server in 5.314 seconds (JVM running for 5.694)
```
####Client Configuration and Deployment
The client is configured to connect to the deployed/started server on `localhost` port `40404`.

To start the client you can decided to run one of the following client parameters:
* `-DproxyClient` - for a JAVA based `look-aside cache` implementation
* `-DlocalCacheClient` - for a JAVA based `near cache` implementation
* `-DproxyClientKT` - for a Kotlin based `look-aside cache` implementation
* `-DlocalCacheClientKT` - for a Kotlin based `near cache` implementation

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

To confirm that the client has completed you should see on of the following outputs:
1. Look-aside caching client output
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Entries on Client: 0
    Entries on Server: 3
      Entry: 
         Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=Me, lastName=My)
      Entry: 
         Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=You, lastName=Yours)
      Entry: 
         Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Third, lastName=Entry)
    Updating entry for key: 2
    Entry Before: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=You, lastName=Yours)
    Entry After: Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=First, lastName=Update)
    Removing entry for key: 3
    Entries:
       Entry: 
          Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=Me, lastName=My)
       Entry: 
          Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=First, lastName=Update)
    ```
2. Near caching client output
    ```
    Inserting 3 entries for keys: 1, 2, 3
    Entries on Client: 3
    Entries on Server: 3
       Entry: 
          Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=Me, lastName=My)
       Entry: 
          Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=You, lastName=Yours)
       Entry: 
          Customer(id=3, emailAddress=EmailAddress(value=5@5.com), firstName=Third, lastName=Entry)
    Updating entry for key: 2
    Entry Before: Customer(id=2, emailAddress=EmailAddress(value=3@3.com), firstName=You, lastName=Yours)
    Entry After: Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=First, lastName=Update)
    Removing entry for key: 3
    Entries:
       Entry: 
          Customer(id=1, emailAddress=EmailAddress(value=2@2.com), firstName=Me, lastName=My)
       Entry: 
          Customer(id=2, emailAddress=EmailAddress(value=4@4.com), firstName=First, lastName=Update)
    ```