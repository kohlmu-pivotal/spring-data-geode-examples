package examples.springdata.geode.client.basic.kt

import examples.springdata.geode.client.basic.kt.config.BasicClientApplicationConfigKT
import examples.springdata.geode.client.basic.kt.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Creates a client to demonstrate basic CRUD operations. This client can be configured in 2 ways, depending on profile
 * selected. "proxy" profile will create a region with PROXY configuration that will store no data locally. "localCache"
 * will create a region that stores data in the local client, to satisfy the "near cache" paradigm.
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [BasicClientApplicationConfigKT::class])
class BasicClientKT(internal val customerServiceKT: CustomerServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(BasicClientKT::class.java, *args)?.apply {
        getBean<BasicClientKT>(BasicClientKT::class).apply {

            println("Inserting 3 entries for keys: 1, 2, 3")
            customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
            customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
            customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))

            println("Entries on Client: ${customerServiceKT.numberEntriesStoredLocally()}")
            println("Entries on Server: ${customerServiceKT.numberEntriesStoredOnServer()}")
            customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }

            println("Updating entry for key: 2")
            println("Entry Before: " + customerServiceKT.findById(2).get())
            customerServiceKT.save(Customer(2, EmailAddress("4@4.com"), "Sam", "Spacey"))
            println("Entry After: " + customerServiceKT.findById(2).get())

            println("Removing entry for key: 3")
            customerServiceKT.deleteById(3)

            println("Entries:")
            customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }
        }
    }
}