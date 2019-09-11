package examples.springdata.geode.client.security.client;

import examples.springdata.geode.client.security.client.config.SecurityEnabledClientConfiguration;
import examples.springdata.geode.client.security.client.services.CustomerService;
import examples.springdata.geode.client.security.server.SecurityEnabledServer;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.apache.geode.cache.Region;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = SecurityEnabledClientConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SecurityEnabledClientTest extends ForkingClientServerIntegrationTestsSupport {

    @Autowired
    private CustomerService customerService;

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(SecurityEnabledServer.class);
    }

    @Test
    public void customersRegionWasConfiguredCorrectly() {
        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isEmpty();
    }

    @Test
    public void customerServiceWasConfiguredCorrectly() {
        assertThat(this.customerService).isNotNull();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {
        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        Customer john = new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith");
        Customer frank = new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport");
        Customer jude = new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons");
        customerService.save(john);
        customerService.save(frank);
        customerService.save(jude);
        assertThat(customers.keySetOnServer().size()).isEqualTo(3);
        System.out.println("Customers saved on server:");
        List<Customer> customerList = customerService.findAll();
        assertThat(customerList.size()).isEqualTo(3);
        assertThat(customerList.contains(john)).isTrue();
        assertThat(customerList.contains(frank)).isTrue();
        assertThat(customerList.contains(jude)).isTrue();
        customerList.forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
    }
}