package examples.springdata.geode.client.cq.kt.producer

import examples.springdata.geode.client.cq.kt.producer.config.CQProducerClientApplicationConfigKT
import examples.springdata.geode.client.cq.kt.producer.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [CQProducerClientApplicationConfigKT::class])
class CQProducerClientKT {
    @Bean
    fun runner(customerServiceKT: CustomerServiceKT) = ApplicationRunner {
        println("Inserting 3 entries for keys: 1, 2, 3")
        customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
        customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
        customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CQProducerClientKT::class.java, *args)
}
