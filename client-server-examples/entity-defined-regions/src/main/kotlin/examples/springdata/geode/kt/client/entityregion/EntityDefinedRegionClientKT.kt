package examples.springdata.geode.kt.client.entityregion

import examples.springdata.geode.kt.client.entityregion.config.EntityDefinedRegionClientConfigKT
import examples.springdata.geode.kt.client.entityregion.service.CustomerServiceKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [EntityDefinedRegionClientConfigKT::class])
class EntityDefinedRegionClientKT(val customerServiceKT: CustomerServiceKT) {
    @Bean
    fun runner(): ApplicationRunner = ApplicationRunner { args ->
        println("Inserting 3 entries for keys: 1, 2, 3")
        customerServiceKT.save(examples.springdata.geode.domain.Customer(1, examples.springdata.geode.domain.EmailAddress("2@2.com"), "John", "Smith"))
        customerServiceKT.save(examples.springdata.geode.domain.Customer(2, examples.springdata.geode.domain.EmailAddress("3@3.com"), "Frank", "Lamport"))
        customerServiceKT.save(examples.springdata.geode.domain.Customer(3, examples.springdata.geode.domain.EmailAddress("5@5.com"), "Jude", "Simmons"))

        println("Entries on Client: ${customerServiceKT.numberEntriesStoredLocally()}")
        println("Entries on Server: ${customerServiceKT.numberEntriesStoredOnServer()}")
        customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }

        println("Updating entry for key: 2")
        println("Entry Before: " + customerServiceKT.findById(2).get())
        customerServiceKT.save(examples.springdata.geode.domain.Customer(2, examples.springdata.geode.domain.EmailAddress("4@4.com"), "Sam", "Spacey"))
        println("Entry After: " + customerServiceKT.findById(2).get())

        println("Removing entry for key: 3")
        customerServiceKT.deleteById(3)

        println("Entries:")
        customerServiceKT.findAll().forEach { customer -> println("\t Entry: \n \t\t $customer") }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EntityDefinedRegionClientKT::class.java)
}