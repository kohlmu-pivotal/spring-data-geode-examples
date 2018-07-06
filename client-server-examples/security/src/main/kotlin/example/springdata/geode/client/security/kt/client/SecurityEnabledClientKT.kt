package example.springdata.geode.client.security.kt.client

import example.springdata.geode.client.security.kt.client.config.SecurityEnabledClientConfigurationKT
import example.springdata.geode.client.security.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [SecurityEnabledClientConfigurationKT::class])
class SecurityEnabledClientKT(val customerRepositoryKT: CustomerRepositoryKT)

fun main(args: Array<String>) {
    SpringApplication.run(SecurityEnabledClientKT::class.java, *args)?.run {
        this.getBean(SecurityEnabledClientKT::class.java)?.run {
            this.customerRepositoryKT.save(Customer(1L, EmailAddress("2@2.com"), "John", "Smith"))
        }
    }
}