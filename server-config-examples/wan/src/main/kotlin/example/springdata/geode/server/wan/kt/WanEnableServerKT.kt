package example.springdata.geode.server.wan.kt

import com.github.javafaker.Faker
import example.springdata.geode.server.wan.config.WanEnableServerConfig
import example.springdata.geode.server.wan.repo.CustomerRepository
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import java.util.stream.LongStream

@SpringBootApplication(scanBasePackageClasses = [WanEnableServerConfig::class])
class WanEnableServerKT {

    @Bean
    @Profile("default", "SiteA")
    fun siteARunner(customerRepository: CustomerRepository) =
            ApplicationRunner {
                createCustomerData(customerRepository)
            }

    @Bean
    @Profile("SiteB")
    fun siteBRunner() =
            ApplicationRunner {
                readLine()
            }

    private fun createCustomerData(customerRepository: CustomerRepository) {
        println("Inserting 3 entries for keys: 1, 2, 3")
        val faker = Faker()
        LongStream.rangeClosed(0, 300)
                .parallel()
                .forEach { customerId ->
                    customerRepository.save(
                            Customer(customerId,
                                    EmailAddress(faker.internet().emailAddress()), faker.name().firstName(), faker.name().lastName()))
                }
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(WanEnableServerKT::class.java)
            .web(WebApplicationType.NONE)
            .build()
            .run(*args)
}