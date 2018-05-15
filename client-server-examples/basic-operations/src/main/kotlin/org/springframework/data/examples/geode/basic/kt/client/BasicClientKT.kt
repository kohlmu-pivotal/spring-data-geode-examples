package org.springframework.data.examples.geode.basic.kt.client

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.domain.EmailAddress

@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.basic.kt.client.**"])
class BasicClientKT(internal val customerServiceKT: CustomerServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(BasicClientKT::class.java, *args)?.apply {
        getBean<BasicClientKT>(BasicClientKT::class).apply {

            println("Inserting 3 entries for keys: 1, 2, 3")
            customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "Me", "My"))
            customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "You", "Yours"))
            customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Third", "Entry"))

            println("Entries on Client: " + customerServiceKT.numberEntriesStoredLocally())
            println("Entries on Server: " + customerServiceKT.numberEntriesStoredOnServer())
            customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }

            println("Updating entry for key: 2")
            println("Entry Before: " + customerServiceKT.findById(2).get())
            customerServiceKT.save(Customer(2, EmailAddress("4@4.com"), "First", "Update"))
            println("Entry After: " + customerServiceKT.findById(2).get())

            println("Removing entry for key: 3")
            customerServiceKT.deleteById(3)

            println("Entries:")
            customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }
        }
    }
}