package org.springframework.data.examples.geode.cq.kt.client.producer

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.cq.kt.client.producer.config.CQProducerClientApplicationConfigKT
import org.springframework.data.examples.geode.cq.kt.client.producer.services.CustomerServiceKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.model.EmailAddress

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [CQProducerClientApplicationConfigKT::class])
class CQProducerClientKT(internal val customerServiceKT: CustomerServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(CQProducerClientKT::class.java, *args)?.apply {
        getBean<CQProducerClientKT>(CQProducerClientKT::class).apply {

            println("Inserting 3 entries for keys: 1, 2, 3")
            customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
            customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
            customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))
        }
    }
}