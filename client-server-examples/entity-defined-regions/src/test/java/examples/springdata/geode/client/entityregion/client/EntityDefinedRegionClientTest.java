package examples.springdata.geode.client.entityregion.client;

import examples.springdata.geode.client.entityregion.client.config.EntityDefinedRegionClientConfig;
import examples.springdata.geode.client.entityregion.client.service.CustomerService;
import examples.springdata.geode.client.entityregion.server.EntityDefinedRegionServer;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = EntityDefinedRegionClientConfig.class)
public class EntityDefinedRegionClientTest extends ForkingClientServerIntegrationTestsSupport {

    @Autowired
    private CustomerService customerService;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(EntityDefinedRegionServer.class);
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

        int localEntries = customerService.numberEntriesStoredLocally();
        assertThat(localEntries).isEqualTo(0);
        System.out.println("Entries on Client: " + localEntries);
        int serverEntries = customerService.numberEntriesStoredOnServer();
        assertThat(serverEntries).isEqualTo(3);
        System.out.println("Entries on Server: " + serverEntries);
        List<Customer> all = customerService.findAll();
        assertThat(all.size()).isEqualTo(3);
        all.forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));

        System.out.println("Updating entry for key: 2");
        Customer customer = customerService.findById(2L).get();
        assertThat(customer).isEqualTo(frank);
        System.out.println("Entry Before: " + customer);
        Customer sam = new Customer(2L, new EmailAddress("4@4.com"), "Sam", "Spacey");
        customerService.save(sam);
        customer = customerService.findById(2L).get();
        assertThat(customer).isEqualTo(sam);
        System.out.println("Entry After: " + customer);

        System.out.println("Removing entry for key: 3");
        customerService.deleteById(3L);
        assertThat(customerService.findById(3L)).isEmpty();

        System.out.println("Entries:");
        all = customerService.findAll();
        assertThat(all.size()).isEqualTo(2);
        all.forEach(c -> System.out.println("\t Entry: \n \t\t " + c));
    }
}