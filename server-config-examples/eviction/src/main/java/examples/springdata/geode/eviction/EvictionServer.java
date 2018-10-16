package examples.springdata.geode.eviction;

import examples.springdata.geode.eviction.config.EvictionServerConfig;
import examples.springdata.geode.eviction.repo.CustomerRepository;
import examples.springdata.geode.eviction.repo.OrderRepository;
import examples.springdata.geode.eviction.repo.ProductRepository;
import examples.springdata.geode.util.DataCreatorsKt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = EvictionServerConfig.class)
public class EvictionServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EvictionServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public ApplicationRunner runner(CustomerRepository customerRepository,
                                    OrderRepository orderRepository,
                                    ProductRepository productRepository) {
        return args -> {
            createCustomerData(customerRepository);
            createProducts(productRepository);
            createOrders(productRepository, orderRepository);
        };
    }

    private void createOrders(ProductRepository productRepository, OrderRepository orderRepository) {
        DataCreatorsKt.createOrders(100, 300, 300, 5, orderRepository, productRepository);
    }

    private void createProducts(ProductRepository productRepository) {
        System.out.println("Inserting 300 products");
        DataCreatorsKt.createProducts(300, productRepository);
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 300 customers");
        DataCreatorsKt.createCustomers(300, customerRepository);
    }
}