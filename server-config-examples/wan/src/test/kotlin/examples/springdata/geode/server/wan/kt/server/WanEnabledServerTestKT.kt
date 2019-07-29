package examples.springdata.geode.server.wan.kt.server

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.wan.kt.client.config.WanClientConfigKT
import examples.springdata.geode.server.wan.kt.server.siteA.WanEnabledServerSiteAKT
import examples.springdata.geode.server.wan.kt.server.siteB.WanEnabledServerSiteBKT
import org.apache.geode.cache.Region
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@ActiveProfiles("wan-integration-test", "test", "default")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [WanClientConfigKT::class])
class WanEnabledServerTestKT : ForkingClientServerIntegrationTestsSupport() {

    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    @Test
    @Throws(IOException::class)
    fun testMethod() {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).until { customers!!.keySetOnServer().size == 301 }
        Assertions.assertThat(customers!!.keySetOnServer().size).isEqualTo(301)
        println(customers.keySetOnServer().size.toString() + " entries replicated to siteB")    }

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(WanEnabledServerSiteBKT::class.java)
            startGemFireServer(WanEnabledServerSiteAKT::class.java)
            System.getProperties().remove("spring.data.gemfire.pool.servers")
        }
    }
}