package examples.springdata.geode.functions.cascading.client;

import examples.springdata.geode.domain.*;
import examples.springdata.geode.functions.cascading.client.config.CascadingFunctionClientConfig;
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
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CascadingFunctionClientConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CascadingFunctionClientTest extends ForkingClientServerIntegrationTestsSupport {
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
    public void customersRegionWasConfiguredCorrectly() {
        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
    }

    @Test
    public void ordersRegionWasConfiguredCorrectly() {
        assertThat(this.orders).isNotNull();
        assertThat(this.orders.getName()).isEqualTo("Orders");
        assertThat(this.orders.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Orders"));
    }

    @Test
    public void productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull();
        assertThat(this.products.getName()).isEqualTo("Products");
        assertThat(this.products.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Products"));
    }

    @Test
    public void functionsExecuteCorrectly() {
        IntStream.rangeClosed(1, 10000).parallel().forEach(customerId ->
                customerService.save(new Customer(Integer.toUnsignedLong(customerId), new EmailAddress("2@2.com"), "John"+customerId, "Smith" + customerId)));

        assertThat(customers.keySetOnServer().size()).isEqualTo(10000);

        productService.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"), "An Apple portable music player"));
        productService.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"), "An Apple tablet device"));

        Product product = new Product(3L, "Apple macBook", new BigDecimal("899.99"), "An Apple notebook computer");
        product.addAttribute("warranty", "included");

        productService.save(product);

        assertThat(products.keySetOnServer().size()).isEqualTo(3);

        Random random = new Random(System.nanoTime());
        Address address = new Address("it", "doesn't", "matter");

        IntStream.rangeClosed(1, 10).forEach(orderId ->
                IntStream.rangeClosed(1, 10).forEach(customerId -> {
                    Order order = new Order(Integer.toUnsignedLong(orderId), customerId, address);
                    IntStream.rangeClosed(1, random.nextInt(3) + 1).forEach(i -> {
                        int quantity = random.nextInt(3) + 1;
                        Long productId = (long) (random.nextInt(3) + 1);
                        order.add(new LineItem(productService.findById(productId), quantity));
                    });
                    orderService.save(order);
                }));

        assertThat(orders.keySetOnServer().size()).isEqualTo(10);

        List<Long> listAllCustomers = customerService.listAllCustomers();
        assertThat(listAllCustomers.size()).isEqualTo(10000);
        System.out.println("Number of customers retrieved from servers: " + listAllCustomers.size());

        List<Order> findOrdersForCustomer = orderService.findOrdersForCustomers(listAllCustomers);
        assertThat(findOrdersForCustomer.size()).isEqualTo(10);
        System.out.println(findOrdersForCustomer);
    }
}