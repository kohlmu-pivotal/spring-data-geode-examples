package examples.springdata.geode.client.common.kt.client

import examples.springdata.geode.client.common.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress

interface BaseClientKT {
    fun populateData(customerServiceKT: CustomerServiceKT) {
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