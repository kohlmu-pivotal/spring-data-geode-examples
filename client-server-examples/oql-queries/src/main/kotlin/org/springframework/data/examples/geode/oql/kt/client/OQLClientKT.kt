package org.springframework.data.examples.geode.oql.kt.client

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.model.EmailAddress

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [OQLClientKT::class])
class OQLClientKT(internal val customerServiceKT: CustomerServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(OQLClientKT::class.java, *args)?.apply {
        getBean<OQLClientKT>(OQLClientKT::class).apply {

            println("Inserting 3 entries for keys: 1, 2, 3")
            customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
            customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
            customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))

            println("Find customer with key=2 using GemFireRepository: " + customerServiceKT.findById(2).get())
            println("Find customer with key=2 using GemFireTemplate: " +
                "${customerServiceKT.findWithTemplate<Customer>("select * from /Customers where id=$1", 2)}")

            customerServiceKT.save(Customer(1, EmailAddress("3@3.com"), "Jude", "Smith"))
            println("Find customers with emailAddress=3@3.com: " + "${customerServiceKT.findByEmailAddressUsingIndex<Customer>("3@3.com")}")

            println("Find customers with firstName=Frank: " + "${customerServiceKT.findByFirstNameUsingIndex<Customer>("Frank")}")
            println("Find customers with firstName=Jude: " + "${customerServiceKT.findByFirstNameUsingIndex<Customer>("Jude")}")

            println("Find customers with firstName=Jude on local client region: " + "${customerServiceKT.findByFirstNameLocalClientRegion<Customer>("select * from /Customers where firstName=$1", "Jude")}")


        }
    }
}