package examples.springdata.geode.client.function.client;

import examples.springdata.geode.client.function.client.config.FunctionInvocationClientApplicationConfig;
import examples.springdata.geode.client.function.client.services.CustomerService;
import examples.springdata.geode.client.function.client.services.OrderService;
import examples.springdata.geode.client.function.client.services.ProductService;
import examples.springdata.geode.client.function.server.FunctionServer;
import examples.springdata.geode.domain.*;
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
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = FunctionInvocationClientApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FunctionInvocationClientTest extends ForkingClientServerIntegrationTestsSupport {

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
        startGemFireServer(FunctionServer.class);
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
    public void orderServiceWasConfiguredCorrectly() {

        assertThat(this.orderService).isNotNull();
    }

    @Test
    public void productServiceWasConfiguredCorrectly() {

        assertThat(this.productService).isNotNull();
    }

    @Test
    public void functionsExecuteCorrectly() {
        createCustomerData();

        List<Customer> cust = customerService.listAllCustomersForEmailAddress("2@2.com", "3@3.com");
        assertThat(cust.size()).isEqualTo(2);
        System.out.println("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t " + cust);

        createProducts();
        BigDecimal sum = productService.sumPricesForAllProducts().get(0);
        assertThat(sum).isEqualTo(BigDecimal.valueOf(1499.97));
        System.out.println("Running function to sum up all product prices: \n\t" + sum);

        createOrders();

        sum = orderService.sumPricesForAllProductsForOrder(1L).get(0);
        assertThat(sum).isGreaterThanOrEqualTo(BigDecimal.valueOf(99.99));
        System.out.println("Running function to sum up all order lineItems prices for order 1: \n\t" + sum);
        Order order = orderService.findById(1L);
        System.out.println("For order: \n\t " + order);
    }

    public void createCustomerData() {

        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
        customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
        customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));
        assertThat(customers.keySetOnServer().size()).isEqualTo(3);
    }

    public void createProducts() {
        productService.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"),
                "An Apple portable music player"));
        productService.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"),
                "An Apple tablet device"));
        Product macbook = new Product(3L, "Apple macBook", new BigDecimal("899.99"),
                "An Apple notebook computer");
        macbook.addAttribute("warranty", "included");
        productService.save(macbook);
        assertThat(products.keySetOnServer().size()).isEqualTo(3);
    }

    public void createOrders() {
        Random random = new Random();
        Address address = new Address("it", "doesn't", "matter");
        LongStream.rangeClosed(1, 100).forEach((orderId) ->
                LongStream.rangeClosed(1, 3).forEach((customerId) -> {
                    Order order = new Order(orderId, customerId, address);
                    IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach((lineItemCount) -> {
                        int quantity = random.nextInt(3) + 1;
                        long productId = random.nextInt(3) + 1;
                        order.add(new LineItem(productService.findById(productId), quantity));
                    });
                    orderService.save(order);
                }));
        assertThat(orders.keySetOnServer().size()).isEqualTo(100);
    }
}