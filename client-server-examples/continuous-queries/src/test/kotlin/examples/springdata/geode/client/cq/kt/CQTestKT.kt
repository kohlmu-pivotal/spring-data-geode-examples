package examples.springdata.geode.client.cq.kt

import examples.springdata.geode.client.common.kt.server.ServerKT
import examples.springdata.geode.client.cq.kt.config.CQClientApplicationConfigKT
import examples.springdata.geode.client.cq.kt.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.apache.geode.cache.Region
import org.apache.geode.cache.query.CqEvent
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport
import org.springframework.data.gemfire.util.RegionUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CQClientApplicationConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CQTestKT : ForkingClientServerIntegrationTestsSupport() {

    private var counter = 0

    @Autowired
    internal var container: ContinuousQueryListenerContainer? = null

    @Autowired
    private val customerService: CustomerServiceKT? = null

    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(ServerKT::class.java)
        }
    }

    @Test
    fun customerServiceWasConfiguredCorrectly() {
        assertThat(this.customerService).isNotNull
    }

    @Test
    fun customersRegionWasConfiguredCorrectly() {
        assertThat(this.customers).isNotNull
        assertThat(this.customers!!.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
    }

    @Test
    fun continuousQueryWorkingCorrectly() {
        assertThat(this.customers).isEmpty()
        println("Inserting 3 entries for keys: 1, 2, 3")
        customerService!!.save(Customer(1L, EmailAddress("2@2.com"), "John", "Smith"))
        customerService.save(Customer(2L, EmailAddress("3@3.com"), "Frank", "Lamport"))
        customerService.save(Customer(3L, EmailAddress("5@5.com"), "Jude", "Simmons"))
        assertThat(customers!!.keySetOnServer().size).isEqualTo(3)

        Awaitility.await().atMost(30, TimeUnit.SECONDS).until { this.counter == 3 }
    }

    @ContinuousQuery(name = "CustomerCQ", query = "SELECT * FROM /Customers")
    fun handleEvent(event: CqEvent) {
        println("Received message for CQ 'CustomerCQ'$event")
        counter++
    }

    @After
    fun tearDown() {
        container!!.queryService.closeCqs()
    }
}