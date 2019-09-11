# Server Config Examples

The examples are focused on the configuration of the [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) server and server components.

The examples consist of a server or servers. Some examples (the WAN examples) do contain simple clients for the purpose of verification. Most tests do not involve a client at all.

Each example's code will be available in both Java and Kotlin.
All examples are configured as a `look-aside cache`, where the client stores no data locally. If you want to configure a `near cache`, where the client stores a subset of the data locally, simply change the region shortcut from `PROXY` to `CACHING_PROXY`.

## Server Configuration and Deployment
You may be run either the Kotlin or Java version of the tests as both should behave the same. This itself serves no purpose other than demonstrating the simple and clean interop between Kotlin and Java.

To run the examples simply run the test, which will start the server and perform operations.