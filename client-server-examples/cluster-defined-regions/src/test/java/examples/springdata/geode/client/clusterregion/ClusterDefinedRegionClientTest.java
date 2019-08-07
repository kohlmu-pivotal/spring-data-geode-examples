package examples.springdata.geode.client.clusterregion;

import examples.springdata.geode.client.clusterregion.client.ClusterDefinedRegionClient;
import examples.springdata.geode.client.clusterregion.client.service.CustomerService;
import examples.springdata.geode.client.clusterregion.client.service.OrderService;
import examples.springdata.geode.client.clusterregion.client.service.ProductService;
import examples.springdata.geode.domain.*;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ClusterDefinedRegionClient.class)
public class ClusterDefinedRegionClientTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Orders")
    private Region<Long, Order> orders;

    @Resource(name = "Products")
    private Region<Long, Product> products;

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
    public void ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull();
        assertThat(this.orders.getName()).isEqualTo("Orders");
        assertThat(this.orders.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Orders"));
        assertThat(this.orders).isEmpty();
    }

    @Test
    public void productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull();
        assertThat(this.products.getName()).isEqualTo("Products");
        assertThat(this.products.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Products"));
        assertThat(this.products).isEmpty();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {

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

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        Address address = new Address("Sesame ", "London", "Japan");
        Order order = new Order(1L, 1L, address, address);

        this.orderService.save(order);

        assertThat(this.orderService.findAll().size()).isEqualTo(1);

        assertThat(this.orderService.findById(1L)).isEqualTo(order);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productService.save(product);

        assertThat(this.productService.findAll().size()).isEqualTo(1);

        assertThat(this.productService.findById(1L)).isEqualTo(product);
    }
}