package examples.springdata.geode.client.basic;

import examples.springdata.geode.client.basic.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.apache.geode.cache.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BasicClientTest {

    @Autowired
    private CustomerService customerService;

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

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

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerService.save(jonDoe);

        assertThat(this.customerService.numberEntriesStoredOnServer()).isEqualTo(3);

        Optional<Customer> jonOptional = this.customerService.findById(15);

        Customer jon2 = null;
        if(jonOptional.isPresent()) {
            jon2 = jonOptional.get();
        }
        assertThat(jon2).isEqualTo(jonDoe);
    }
}