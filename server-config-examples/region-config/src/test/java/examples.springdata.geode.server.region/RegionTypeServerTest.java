package examples.springdata.geode.server.region;

import examples.springdata.geode.domain.*;
import examples.springdata.geode.server.region.repo.CustomerRepository;
import examples.springdata.geode.server.region.repo.OrderRepository;
import examples.springdata.geode.server.region.repo.ProductRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RegionTypeServer.class)
public class RegionTypeServerTest {
    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Orders")
    private Region<Long, Order> orders;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isNotEmpty();
    }

    @Test
    public void ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull();
        assertThat(this.orders.getName()).isEqualTo("Orders");
        assertThat(this.orders.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Orders"));
        assertThat(this.orders).isNotEmpty();
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

        Customer jonDoe = new Customer(4000L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerRepository.save(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(3002);

        Optional<Customer> jonOptional = this.customerRepository.findById(4000L);

        Customer jon2 = null;
        if (jonOptional.isPresent()) {
            jon2 = jonOptional.get();
        }
        assertThat(jon2).isEqualTo(jonDoe);

        customerRepository.delete(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(3001);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(15L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productRepository.save(product);

        assertThat(this.productRepository.count()).isEqualTo(4);

        assertThat(this.productRepository.findById(15L).get()).isEqualTo(product);

        this.productRepository.delete(product);

        assertThat(this.productRepository.count()).isEqualTo(3);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {

        Order order = new Order(200L, 1L,
                new Address("A", "Seattle", "Canada"),
                new Address("B", "San Diego", "Mexico"));

        this.orderRepository.save(order);

        assertThat(this.orderRepository.count()).isEqualTo(101);

        assertThat(this.orderRepository.findById(200L).get()).isEqualTo(order);

        orderRepository.delete(order);

        assertThat(this.orderRepository.count()).isEqualTo(100);
    }
}