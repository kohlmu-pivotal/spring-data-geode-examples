package example.springdata.geode.client.transactions.kt.client

import example.springdata.geode.client.transactions.kt.client.config.TransactionalClientConfigKT
import example.springdata.geode.client.transactions.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [TransactionalClientConfigKT::class])
class TransactionalClientKT {

    @Bean
    internal fun runner(customerService: CustomerServiceKT) = ApplicationRunner {
        println("Number of Entries stored before = " + customerService.numberEntriesStoredOnServer())
        customerService.createFiveCustomers()
        println("Number of Entries stored after = " + customerService.numberEntriesStoredOnServer())
        println("Customer for ID before (transaction commit success) = " + customerService.findById(2L))
        customerService.updateCustomersSuccess()
        println("Customer for ID after (transaction commit success) = " + customerService.findById(2L))
        try {
            customerService.updateCustomersFailure()
        } catch (exception: IllegalArgumentException) {
        }

        println("Customer for ID after (transaction commit failure) = " + customerService.findById(2L))

        customerService.updateCustomersWithDelay(1000, Customer(2L, EmailAddress("2@2.com"), "Numpty", "Hamilton"))
        customerService.updateCustomersWithDelay(10, Customer(2L, EmailAddress("2@2.com"), "Frumpy", "Hamilton"))
        println("Customer for ID after 2 updates with delay = " + customerService.findById(2L))
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(TransactionalClientKT::class.java)
        .web(WebApplicationType.NONE)
        .build()
        .run(*args)
}
