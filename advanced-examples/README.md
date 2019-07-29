# AdvancedExamples

These examples will show case higher-level use cases and how Spring Data for [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) simplifies the implementation of the use case.

The examples are broken up into client and server code. The test will act as the client.

Each example's code will be available in both Java and Kotlin.
All examples are configured as a `look-aside cache`, where the client stores no data locally. If you want to configure a `near cache`, where the client stores a subset of the data locally, simply change the region shortcut from `PROXY` to `CACHING_PROXY`.

## Client Configuration and Deployment
You may be run either the Kotlin or Java version of the tests as both should behave the same. This itself serves no purpose other than demonstrating the simple and clean interop between Kotlin and Java.

To run the examples simply run the test, which will start the server and perform client operations.