package examples.springdata.geode.client.security.kt.client

import examples.springdata.geode.client.security.kt.client.config.SecurityEnabledClientConfigurationKT
import examples.springdata.geode.client.security.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [SecurityEnabledClientConfigurationKT::class])
class SecurityEnabledClientKT(val customerRepositoryKT: CustomerRepositoryKT) {
    @Bean
    fun runner() = ApplicationRunner {
        customerRepositoryKT.save(Customer(1L, EmailAddress("2@2.com"), "John", "Smith"))
        println("Customers saved on server:")
        customerRepositoryKT.findAll().forEach { customer -> println("\t\t $customer") }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SecurityEnabledClientKT::class.java, *args)
}