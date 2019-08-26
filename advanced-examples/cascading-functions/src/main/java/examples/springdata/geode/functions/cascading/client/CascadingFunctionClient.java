package examples.springdata.geode.functions.cascading.client;

import examples.springdata.geode.domain.*;
import examples.springdata.geode.functions.cascading.client.config.CascadingFunctionClientConfig;
import examples.springdata.geode.functions.cascading.client.services.CustomerService;
import examples.springdata.geode.functions.cascading.client.services.OrderService;
import examples.springdata.geode.functions.cascading.client.services.ProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication(scanBasePackageClasses = CascadingFunctionClientConfig.class)
public class CascadingFunctionClient {

    public static void main(String[] args) {
        SpringApplication.run(CascadingFunctionClient.class, args);
    }

    @Bean
    ApplicationRunner runner(CustomerService customerService, ProductService productService, OrderService orderService) {
        return args -> {
            createCustomerData(customerService);

            createProducts(productService);

            createOrders(productService, orderService);

            List<Long> listAllCustomers = customerService.listAllCustomers();
            System.out.println("Number of customers retrieved from servers: " + listAllCustomers.size());

            List<Order> findOrdersForCustomer = orderService.findOrdersForCustomers(listAllCustomers);

            System.out.println(findOrdersForCustomer);
        };
    }

    private void createCustomerData(CustomerService customerService) {
        IntStream.rangeClosed(1, 10000).parallel().forEach( customerId ->
            customerService.save(new Customer(Integer.toUnsignedLong(customerId), new EmailAddress("2@2.com"), "John"+customerId, "Smith" + customerId)));
    }

    private void createProducts(ProductService productService) {
        productService.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"), "An Apple portable music player"));
        productService.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"), "An Apple tablet device"));

        Product product = new Product(3L, "Apple macBook", new BigDecimal("899.99"), "An Apple notebook computer");
        product.addAttribute("warranty", "included");

        productService.save(product);
    }

    private void createOrders(ProductService productService, OrderService orderService) {
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
    }
}