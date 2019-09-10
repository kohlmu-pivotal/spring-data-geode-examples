package examples.springdata.geode.client.oql;

import examples.springdata.geode.client.common.server.Server;
import examples.springdata.geode.client.oql.config.OQLClientApplicationConfig;
import examples.springdata.geode.client.oql.services.CustomerService;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = OQLClientApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OQLClientTest extends ForkingClientServerIntegrationTestsSupport {

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
        assertThat(this.customers).isEmpty();
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

        Customer customer = customerService.findById(2L).get();
        assertThat(customer).isEqualTo(frank);
        System.out.println("Find customer with key=2 using GemFireRepository: " + customer);
        List customerList = customerService.findWithTemplate("select * from /Customers where id=$1", 2L);
        assertThat(customerList.size()).isEqualTo(1);
        assertThat(customerList.contains(frank)).isTrue();
        System.out.println("Find customer with key=2 using GemFireTemplate: " + customerList);

        customer = new Customer(1L, new EmailAddress("3@3.com"), "Jude", "Smith");
        customerService.save(customer);
        assertThat(customers.keySetOnServer().size()).isEqualTo(3);

        customerList = customerService.findByEmailAddress("3@3.com");
        assertThat(customerList.size()).isEqualTo(2);
        assertThat(customerList.contains(frank)).isTrue();
        assertThat(customerList.contains(customer)).isTrue();
        System.out.println("Find customers with emailAddress=3@3.com: " + customerList);

        customerList = customerService.findByFirstNameUsingIndex("Frank");
        assertThat(customerList.get(0)).isEqualTo(frank);
        System.out.println("Find customers with firstName=Frank: " + customerList);
        customerList = customerService.findByFirstNameUsingIndex("Jude");
        assertThat(customerList.size()).isEqualTo(2);
        assertThat(customerList.contains(jude)).isTrue();
        assertThat(customerList.contains(customer)).isTrue();
        System.out.println("Find customers with firstName=Jude: " + customerList);

        customerList = customerService.findByFirstNameLocalClientRegion("select * from /Customers where firstName=$1", "Jude");
        assertThat(customerList).isEmpty();
        System.out.println("Find customers with firstName=Jude on local client region: " + customerList);
    }
}