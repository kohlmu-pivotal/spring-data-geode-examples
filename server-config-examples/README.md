# Server Config Examples

The examples are aimed at show casing the configuration of the [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) server and server components.

The examples are primarily server code. The test will act as the client only enough to validate the functionality of the server.

Each example's code will be available in both Java and Kotlin.
All examples are configured to be `look-aside caches`, where the client stores no data locally. If you want to configure a `near cache`, where the client would stores a subset of the data locally, simply change the region shortcut from `PROXY` to `CACHING_PROXY`.

## Client Configuration and Deployment
You may be run either the Kotlin or Java version of the tests as both should behave the same. This itself serves no purpose other than demonstrating the simple and clean interop between Kotlin and Java.

To run the examples simply run the test, which will start the server.