package org.springframework.data.examples.geode.cq.kt.client.consumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries

/**
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication
@EnableContinuousQueries
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
    retryAttempts = 1, subscriptionEnabled = true, locators = [ClientCacheApplication.Locator(host = "localhost", port = 10334)],
    readyForEvents = true, durableClientId = "23", durableClientTimeout = 10)
class CQConsumerClientKT

fun main(args: Array<String>) {
    SpringApplication.run(CQConsumerClientKT::class.java, *args)
    readLine()
}