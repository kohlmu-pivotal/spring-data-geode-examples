package examples.springdata.geode.server.wan.server;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.wan.client.config.WanClientConfig;
import examples.springdata.geode.server.wan.server.siteA.WanEnabledServerSiteA;
import examples.springdata.geode.server.wan.server.siteB.WanEnabledServerSiteB;
import org.apache.geode.cache.Region;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ActiveProfiles({"wan-integration-test", "test", "default"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = WanClientConfig.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class WanEnabledServerTest extends ForkingClientServerIntegrationTestsSupport {

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(WanEnabledServerSiteB.class,"-Dspring.main.allow-bean-definition-overriding=true");
        startGemFireServer(WanEnabledServerSiteA.class,"-Dspring.main.allow-bean-definition-overriding=true");
        System.getProperties().remove("spring.data.gemfire.pool.servers");
    }

    @Test
    public void testMethod() throws IOException {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(()-> customers.keySetOnServer().size() == 301);
        Assertions.assertThat(customers.keySetOnServer().size()).isEqualTo(301);
        System.out.println(customers.keySetOnServer().size() + " entries replicated to siteB");
    }
}