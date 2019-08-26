package examples.springdata.geode.functions.cascading.client;

import examples.springdata.geode.domain.*;
import examples.springdata.geode.functions.cascading.client.services.CustomerService;
import examples.springdata.geode.functions.cascading.client.services.OrderService;
import examples.springdata.geode.functions.cascading.client.services.ProductService;
import examples.springdata.geode.functions.cascading.server.CascadingFunctionServer;
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
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport.startGemFireServer;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CascadingFunctionClient.class)
public class CascadingFunctionClientTest {
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

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(CascadingFunctionServer.class);
    }

    @Test
    public void customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull();
    }

    @Test
    public void orderServiceWasConfiguredCorrectly() {

        assertThat(this.orderService).isNotNull();
    }

    @Test
    public void productServiceWasConfiguredCorrectly() {

        assertThat(this.productService).isNotNull();
    }

    @Test
    public void customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isEmpty();
        assertThat(this.customers.keySetOnServer().size()).isEqualTo(10000);
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
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        Address address = new Address("Sesame ", "London", "Japan");
        Order order = new Order(1L, 1L, address, address);

        this.orderService.save(order);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productService.save(product);

        assertThat(this.productService.findById(1L)).isEqualTo(product);
    }
}