package examples.springdata.geode.expiration.custom.kt

import com.github.javafaker.Faker
import com.jayway.awaitility.Awaitility
import examples.springdata.geode.domain.Address
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import examples.springdata.geode.expiration.custom.kt.config.CustomExpiryServerConfigKT
import examples.springdata.geode.expiration.custom.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.format
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootApplication(scanBasePackageClasses = [CustomExpiryServerConfigKT::class])
class CustomExpiryServerKT {

    @Bean
    fun createDataFaker(): Faker = Faker()

    @Bean
    fun runner(customerRepositoryKT: CustomerRepositoryKT, faker: Faker) = ApplicationRunner {
        customerRepositoryKT.save(Customer(3L, EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())))

        val conditionEvaluator = { !customerRepositoryKT.findById(3L).isPresent }


        val formatPattern = "dd/MM/yyyy hh:mm:ss:SSS"
        println("Starting TTL wait period: ${Date().format(formatPattern)}")
        //Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
        // will be used.
        Awaitility.await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until<Any>(conditionEvaluator)

        println("Ending TTL wait period: ${Date().format(formatPattern)}")

        customerRepositoryKT.save(Customer(3L, EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())))

        println("Starting Idle wait period: ${Date().format(formatPattern)}")

        //Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
        Awaitility.await()
                .pollDelay(2, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until<Any>(conditionEvaluator)

        println("Ending Idle wait period:${Date().format(formatPattern)}")
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(CustomExpiryServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}
