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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@ActiveProfiles("wan-integration-test", "test", "default")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [WanClientConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class WanEnabledServerTestKT : ForkingClientServerIntegrationTestsSupport() {

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Test
    fun wanReplicationOccurs() {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until { customers.keySetOnServer().size == 301 }
        Assertions.assertThat(customers.keySetOnServer().size).isEqualTo(301)
        println(customers.keySetOnServer().size.toString() + " entries replicated to siteB")
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startGemFireServer(WanEnabledServerSiteBKT::class.java,"-Dspring.main.allow-bean-definition-overriding=true")
            startGemFireServer(WanEnabledServerSiteAKT::class.java,"-Dspring.main.allow-bean-definition-overriding=true")
            System.getProperties().remove("spring.data.gemfire.pool.servers")
        }
    }
}