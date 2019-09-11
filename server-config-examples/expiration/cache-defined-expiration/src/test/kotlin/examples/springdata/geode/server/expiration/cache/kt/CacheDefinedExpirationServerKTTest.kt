package examples.springdata.geode.server.expiration.cache.kt

import com.github.javafaker.Faker
import com.jayway.awaitility.Awaitility
import examples.springdata.geode.domain.Address
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import examples.springdata.geode.server.expiration.cache.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.format
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CacheDefinedExpirationServerKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CacheDefinedExpirationServerKTTest {

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Autowired
    lateinit var faker: Faker

    @Test
    fun evictionIsConfiguredCorrectly() {
        customerRepository.save(Customer(1L, EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())))

        assertThat(customerRepository.count()).isEqualTo(1)

        val conditionEvaluator = { !customerRepository.findById(1L).isPresent }

        val formatPattern = "dd/MM/yyyy hh:mm:ss:SSS"
        println("Starting TTL wait period: ${Date().format(formatPattern)}")
        //Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
        // will be used.
        Awaitility.await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until<Any>(conditionEvaluator)

        assertThat(customerRepository.count()).isEqualTo(0)

        println("Ending TTL wait period: ${Date().format(formatPattern)}")

        customerRepository.save(Customer(1L, EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())))

        assertThat(customerRepository.count()).isEqualTo(1)

        println("Starting Idle wait period: ${Date().format(formatPattern)}")

        //Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
        Awaitility.await()
                .pollDelay(2, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until<Any>(conditionEvaluator)

        assertThat(customerRepository.count()).isEqualTo(0)

        println("Ending Idle wait period:${Date().format(formatPattern)}")
    }
}