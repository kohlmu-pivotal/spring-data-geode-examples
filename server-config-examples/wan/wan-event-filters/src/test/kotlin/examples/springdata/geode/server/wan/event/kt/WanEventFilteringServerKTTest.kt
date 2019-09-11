package examples.springdata.geode.server.wan.event.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.wan.kt.client.config.WanClientConfigKT
import org.apache.geode.cache.Region
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [WanClientConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class WanEventFilteringServerKTTest : ForkingClientServerIntegrationTestsSupport() {

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Test
    fun wanReplicationOccursCorrectly() {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until { customers.keySetOnServer().size == 150 }
        assertThat(customers.keySetOnServer().size).isEqualTo(150)
        println(customers.keySetOnServer().size.toString() + " entries replicated to siteA")
    }

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(WanEventFilteringServerKT::class.java, "-Dspring.profiles.active=SiteB")
            startGemFireServer(WanEventFilteringServerKT::class.java, "-Dspring.profiles.active=SiteA")
            System.getProperties().remove("spring.data.gemfire.pool.servers")
        }
    }
}