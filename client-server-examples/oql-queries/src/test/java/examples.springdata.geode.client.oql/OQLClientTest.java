package examples.springdata.geode.client.oql;

import examples.springdata.geode.client.common.server.Server;
import examples.springdata.geode.client.oql.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.apache.geode.cache.Region;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport.startGemFireServer;

@ActiveProfiles({"test", "default"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OQLClientTest {

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

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerService.save(jonDoe);

        Customer jon2 = (Customer)this.customerService.findByEmailAddress("example@example.org").get(0);
        assertThat(jon2).isEqualTo(jonDoe);

        jon2 = (Customer) this.customerService.findByFirstNameUsingIndex("Jon").get(0);
        assertThat(jon2).isEqualTo(jonDoe);

        jon2 = this.customerService.findById(15L).get();
        assertThat(jon2).isEqualTo(jonDoe);

        String query = "select * from /Customers customer where customer.lastName=$1";
        jon2 = (Customer) this.customerService.findWithTemplate(query, "Doe").get(0);
        assertThat(jon2).isEqualTo(jonDoe);

        assertThat(this.customerService.findByFirstNameLocalClientRegion(query, "Doe")).isEmpty();
    }
}