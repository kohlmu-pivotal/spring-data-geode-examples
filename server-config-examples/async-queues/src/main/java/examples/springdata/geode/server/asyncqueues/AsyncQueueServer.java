package examples.springdata.geode.server.asyncqueues;

import examples.springdata.geode.server.asyncqueues.config.AsyncQueueServerConfig;
import examples.springdata.geode.server.asyncqueues.repo.CustomerRepository;
import examples.springdata.geode.server.asyncqueues.repo.OrderProductSummaryRepository;
import examples.springdata.geode.server.asyncqueues.repo.OrderRepository;
import examples.springdata.geode.server.asyncqueues.repo.ProductRepository;
import examples.springdata.geode.domain.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootApplication(scanBasePackageClasses = AsyncQueueServerConfig.class)
public class AsyncQueueServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AsyncQueueServer.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }

    @Bean
    public ApplicationRunner runner(CustomerRepository customerRepository, OrderRepository orderRepository,
                                    ProductRepository productRepository, OrderProductSummaryRepository orderProductSummaryRepository) {
        return args -> {
            createCustomerData(customerRepository);

            createProducts(productRepository);

            createOrders(productRepository, orderRepository);

            System.out.println("Completed creating orders ");

            final List<OrderProductSummary> allForProductID = orderProductSummaryRepository.findAllForProductID(3L);
            allForProductID.forEach(orderProductSummary -> System.out.println("orderProductSummary = " + orderProductSummary));

            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }

    private void createOrders(ProductRepository productRepository, OrderRepository orderRepository) {
        Random random = new Random(System.nanoTime());
        Address address = new Address("it", "doesn't", "matter");
        LongStream.rangeClosed(1, 10).forEach((orderId) ->
            LongStream.rangeClosed(1, 300).forEach((customerId) -> {
                Order order = new Order(orderId, customerId, address);
                IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach((lineItemCount) -> {
                    int quantity = random.nextInt(3) + 1;
                    long productId = random.nextInt(3) + 1;
                    order.add(new LineItem(productRepository.findById(productId).get(), quantity));
                });
                orderRepository.save(order);
            }));
    }

    private void createProducts(ProductRepository productRepository) {
        productRepository.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"),
                "An Apple portable music player"));
        productRepository.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"),
                "An Apple tablet device"));
        Product macbook = new Product(3L, "Apple macBook", new BigDecimal("899.99"),
                        "An Apple notebook computer");
        macbook.addAttribute("warranty", "included");
        productRepository.save(macbook);
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        LongStream.rangeClosed(0, 300)
            .parallel()
            .forEach(customerId ->
                customerRepository.save(new Customer(customerId, new EmailAddress(customerId + "@2.com"), "John" + customerId, "Smith" + customerId)));
    }
}