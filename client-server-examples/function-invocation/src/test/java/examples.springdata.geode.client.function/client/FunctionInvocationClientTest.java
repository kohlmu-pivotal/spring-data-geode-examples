package examples.springdata.geode.client.function.client;

import examples.springdata.geode.client.function.client.services.CustomerService;
import examples.springdata.geode.client.function.client.services.OrderService;
import examples.springdata.geode.client.function.client.services.ProductService;
import examples.springdata.geode.client.function.server.FunctionServer;
import examples.springdata.geode.domain.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport.startGemFireServer;

@ActiveProfiles({"test", "default"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FunctionInvocationClientTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(FunctionServer.class);
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
    public void customerRepositoryWasAutoConfiguredCorrectly() {

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerService.save(jonDoe);

        Customer jon2 = this.customerService.listAllCustomersForEmailAddress("example@example.org").get(0);
        assertThat(jon2).isEqualTo(jonDoe);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        Address address = new Address("Sesame ", "London", "Japan");
        Order order = new Order(1L, 1L, address, address);
        order.add(new LineItem(new Product(1L, "Magic Beans", new BigDecimal(5))));

        this.orderService.save(order);

        assertThat(this.orderService.findById(1L)).isEqualTo(order);
    }

    @Test
    public void sumPricesForAllProductsForOrderTest() {
        assertThat(this.orderService.sumPricesForAllProductsForOrder(1L).get(0).doubleValue()).isEqualTo(5);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productService.save(product);

        assertThat(this.productService.findById(1L)).isEqualTo(product);
    }

    @Test
    public void sumPricesForAllProductsTest() {
        assertThat(this.productService.sumPricesForAllProducts().get(0).doubleValue()).isEqualTo(1499.97);
    }
}