package examples.springdata.geode.server.wan.kt.server.siteA

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import examples.springdata.geode.server.wan.kt.server.repo.CustomerRepositoryKT
import examples.springdata.geode.server.wan.kt.server.siteA.config.WanEnabledServerSiteAConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication(scanBasePackageClasses = [WanEnabledServerSiteAConfigKT::class])
class WanEnabledServerSiteAKT {
    @Bean
    fun siteARunner(customerRepository: CustomerRepositoryKT, faker: Faker) = ApplicationRunner {
        createCustomerData(customerRepository,faker)
        Scanner(System.`in`).nextLine()
    }

    private fun createCustomerData(customerRepository: CustomerRepositoryKT, faker: Faker) {
        println("Inserting 301 entries on siteA")
        (0..300L).forEach { customerId ->
            customerRepository.save(
                    Customer(customerId,
                            EmailAddress(faker.internet().emailAddress()), faker.name().firstName(), faker.name().lastName()))
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(WanEnabledServerSiteAKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }
}