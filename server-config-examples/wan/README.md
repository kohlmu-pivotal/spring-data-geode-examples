# WAN Example

In this example two [Pivotal GemFire](https://pivotal.io/pivotal-gemfire) / [Apache Geode](http://geode.apache.org/) servers are deploy. One server populates itself with data and the other server gets populated with that data via WAN replication.

This example is a little different from the others because there are two servers instead of one. The two servers will be referred to as siteA and siteB. The test will connect to siteB to validate that entries are replicated to it.

To run the example simply run the tests located under wan/src/test in your IDE.

## Running the example

The example is broken up into multiple steps:
1. siteA inserts (Put) 300 Customer entries into the `Customers` region using the repositories `save` method.
2. Print the number of entries replicated to siteB

Your test output should contain output similar to the following:

    [FORK] - Inserting 301 entries on siteA
    
    301 entries replicated to siteB