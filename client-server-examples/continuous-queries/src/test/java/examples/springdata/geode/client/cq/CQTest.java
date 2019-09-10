package examples.springdata.geode.client.cq;

import examples.springdata.geode.client.common.server.Server;
import examples.springdata.geode.client.cq.config.CQClientApplicationConfig;
import examples.springdata.geode.client.cq.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.CqEvent;
import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CQClientApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CQTest extends ForkingClientServerIntegrationTestsSupport {

    private int counter = 0;

    @Autowired
    ContinuousQueryListenerContainer container;

    @Autowired
    private CustomerService customerService;

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(Server.class);
    }

    @Test
    public void customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull();
    }

    @Test
    public void customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
    }

    @Test
    public void continuousQueryWorkingCorrectly() {
        assertThat(this.customers).isEmpty();
        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
        customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
        customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));
        assertThat(customers.keySetOnServer().size()).isEqualTo(3);

        Awaitility.await().atMost(30, TimeUnit.SECONDS).until(()-> this.counter == 3);
    }

    @ContinuousQuery(name = "CustomerCQ", query = "SELECT * FROM /Customers")
    public void handleEvent(CqEvent event) {
        System.out.println("Received message for CQ 'CustomerCQ'" + event);
        counter++;
    }

    @After
    public void tearDown() {
        container.getQueryService().closeCqs();
    }
}