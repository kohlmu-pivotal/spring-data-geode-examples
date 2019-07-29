package examples.springdata.geode.server.eventhandlers;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.eventhandlers.repo.CustomerRepository;
import examples.springdata.geode.server.eventhandlers.repo.ProductRepository;
import org.apache.geode.cache.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = EventHandlerServer.class)
public class EventHandlerServerTest {
    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isNotEmpty();
    }

    @Test
    public void productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull();
        assertThat(this.products.getName()).isEqualTo("Products");
        assertThat(this.products.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Products"));
        assertThat(this.products).isNotEmpty();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.customerRepository.count()).isEqualTo(3);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.productRepository.count()).isEqualTo(4);
    }
}