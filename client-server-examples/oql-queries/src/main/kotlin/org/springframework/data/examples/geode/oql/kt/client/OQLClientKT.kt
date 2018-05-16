package org.springframework.data.examples.geode.oql.kt.client

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.domain.EmailAddress
import org.springframework.data.examples.geode.oql.kt.repository.CustomerRepositoryKT

/**
 * Creates a client to demonstrate basic CRUD operations. This client can be configured in 2 ways, depending on profile
 * selected. "proxy" profile will create a region with PROXY configuration that will store no data locally. "localCache"
 * will create a region that stores data in the local client, to satisfy the "near cache" paradigm.
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.oql.kt.client.**",
    "org.springframework.data.examples.geode.oql.kt.repository.**"])
class OQLClientKT(internal val customerRepositoryKT: CustomerRepositoryKT)

fun main(args: Array<String>) {
    SpringApplication.run(OQLClientKT::class.java, *args)?.apply {
        getBean<OQLClientKT>(OQLClientKT::class).apply {

            println("Inserting 3 entries for keys: 1, 2, 3")
            customerRepositoryKT.save(Customer(1, EmailAddress("2@2.com"), "Me", "My"))
            customerRepositoryKT.save(Customer(2, EmailAddress("3@3.com"), "You", "Yours"))
            customerRepositoryKT.save(Customer(3, EmailAddress("5@5.com"), "Third", "Entry"))

            println("Find : " + customerRepositoryKT.findById(2).get())

            customerRepositoryKT.save(Customer(2, EmailAddress("4@4.com"), "First", "Update"))
            println("Entry After: " + customerRepositoryKT.findById(2).get())

            println("Removing entry for key: 3")
            customerRepositoryKT.deleteById(3)

            println("Entries:")
            customerRepositoryKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }
        }
    }
}