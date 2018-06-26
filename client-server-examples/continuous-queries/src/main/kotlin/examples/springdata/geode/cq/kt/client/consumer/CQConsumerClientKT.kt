package examples.springdata.geode.cq.kt.client.consumer

import org.apache.geode.cache.query.CqEvent
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery

/**
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [CQConsumerClientKT::class])
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = true, locators = [ClientCacheApplication.Locator(host = "localhost", port = 10334)],
        readyForEvents = true, durableClientId = "22", durableClientTimeout = 5)
@EnableContinuousQueries
class CQConsumerClientKT {

    @ContinuousQuery(name = "CustomerJudeCQ", query = "SELECT * FROM /Customers", durable = true)
    fun handleEvent(event: CqEvent) {
        println("Received message for CQ 'CustomerJudeCQ': $event")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CQConsumerClientKT::class.java, *args)
    readLine()
}