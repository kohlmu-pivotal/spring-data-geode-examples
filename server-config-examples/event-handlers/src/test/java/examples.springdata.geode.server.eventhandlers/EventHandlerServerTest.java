package examples.springdata.geode.server.eventhandlers;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
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
import java.math.BigDecimal;
import java.util.Optional;

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

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerRepository.save(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(4);

        Optional<Customer> jonOptional = this.customerRepository.findById(15L);

        Customer jon2 = null;
        if (jonOptional.isPresent()) {
            jon2 = jonOptional.get();
        }
        assertThat(jon2).isEqualTo(jonDoe);

        customerRepository.delete(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(3);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(15L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productRepository.save(product);

        assertThat(this.productRepository.count()).isEqualTo(5);

        assertThat(this.productRepository.findById(15L).get()).isEqualTo(product);

        this.productRepository.delete(product);

        assertThat(this.productRepository.count()).isEqualTo(4);
    }
}