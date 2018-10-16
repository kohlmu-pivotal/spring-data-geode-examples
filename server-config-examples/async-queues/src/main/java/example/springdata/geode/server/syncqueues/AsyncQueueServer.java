package example.springdata.geode.server.syncqueues;

import example.springdata.geode.server.syncqueues.config.AsyncQueueServerConfig;
import example.springdata.geode.server.syncqueues.repo.CustomerRepository;
import example.springdata.geode.server.syncqueues.repo.OrderProductSummaryRepository;
import example.springdata.geode.server.syncqueues.repo.OrderRepository;
import example.springdata.geode.server.syncqueues.repo.ProductRepository;
import examples.springdata.geode.domain.OrderProductSummary;
import examples.springdata.geode.util.DataCreatorsKt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;

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
        };
    }

    private void createOrders(ProductRepository productRepository, OrderRepository orderRepository) {
        DataCreatorsKt.createOrders(10, 300, 300, 3, orderRepository, productRepository);
    }

    private void createProducts(ProductRepository productRepository) {
        DataCreatorsKt.createProducts(3, productRepository);
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 300 customers");
        DataCreatorsKt.createCustomers(300, customerRepository);
    }
}
