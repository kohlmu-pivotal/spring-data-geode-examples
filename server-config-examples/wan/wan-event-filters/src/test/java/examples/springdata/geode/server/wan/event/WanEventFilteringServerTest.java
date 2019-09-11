package examples.springdata.geode.server.wan.event;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.wan.client.config.WanClientConfig;
import org.apache.geode.cache.Region;
import org.awaitility.Awaitility;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = WanClientConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class WanEventFilteringServerTest extends ForkingClientServerIntegrationTestsSupport {

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(WanEventFilteringServer.class, "-Dspring.profiles.active=SiteB");
        startGemFireServer(WanEventFilteringServer.class, "-Dspring.profiles.active=SiteA");
        System.getProperties().remove("spring.data.gemfire.pool.servers");
    }

    @Test
    public void  wanReplicationOccursCorrectly() {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(()-> customers.keySetOnServer().size() == 150);
        assertThat(customers.keySetOnServer().size()).isEqualTo(150);
        System.out.println(customers.keySetOnServer().size() + " entries replicated to siteA");
    }
}
