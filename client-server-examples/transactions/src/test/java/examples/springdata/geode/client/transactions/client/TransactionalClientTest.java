package examples.springdata.geode.client.transactions.client;

import examples.springdata.geode.client.transactions.client.config.TransactionalClientConfig;
import examples.springdata.geode.client.transactions.client.service.CustomerService;
import examples.springdata.geode.client.transactions.server.TransactionalServer;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TransactionalClientConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TransactionalClientTest extends ForkingClientServerIntegrationTestsSupport {

    @Autowired
    private CustomerService customerService;

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(TransactionalServer.class);
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
    public void repositoryWasAutoConfiguredCorrectly() {

        System.out.println("Number of Entries stored before = " + customerService.numberEntriesStoredOnServer());
        customerService.createFiveCustomers();
        assertThat(customerService.numberEntriesStoredOnServer()).isEqualTo(5);
        System.out.println("Number of Entries stored after = " + customerService.numberEntriesStoredOnServer());
        System.out.println("Customer for ID before (transaction commit success) = " + customerService.findById(2L).get());
        customerService.updateCustomersSuccess();
        assertThat(customerService.numberEntriesStoredOnServer()).isEqualTo(5);
        Customer customer = customerService.findById(2L).get();
        assertThat(customer.getFirstName()).isEqualTo("Humpty");
        System.out.println("Customer for ID after (transaction commit success) = " + customer);
        try {
            customerService.updateCustomersFailure();
        } catch (IllegalArgumentException exception) {
        }
        customer = customerService.findById(2L).get();
        assertThat(customer.getFirstName()).isEqualTo("Humpty");
        System.out.println("Customer for ID after (transaction commit failure) = " + customerService.findById(2L).get());

        Customer numpty = new Customer(2L, new EmailAddress("2@2.com"), "Numpty", "Hamilton");
        Customer frumpy = new Customer(2L, new EmailAddress("2@2.com"), "Frumpy", "Hamilton");
        customerService.updateCustomersWithDelay(1000, numpty);
        customerService.updateCustomersWithDelay(10, frumpy);
        customer = customerService.findById(2L).get();
        assertThat(customer).isEqualTo(frumpy);
        System.out.println("Customer for ID after 2 updates with delay = " + customer);
    }
}