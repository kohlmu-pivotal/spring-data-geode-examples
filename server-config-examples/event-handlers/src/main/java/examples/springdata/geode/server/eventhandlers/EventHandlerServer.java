package examples.springdata.geode.server.eventhandlers;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.eventhandlers.config.EventHandlerServerConfiguration;
import examples.springdata.geode.server.eventhandlers.repo.CustomerRepository;
import examples.springdata.geode.server.eventhandlers.repo.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.LongStream;

@SpringBootApplication(scanBasePackageClasses = EventHandlerServerConfiguration.class)
public class EventHandlerServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EventHandlerServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    ApplicationRunner runner(CustomerRepository customerRepository, ProductRepository productRepository) {
        return args -> {
            createCustomerData(customerRepository);
            createProducts(productRepository);

            final Optional<Product> product = productRepository.findById(5L);
            System.out.println("product = " + product.get());
        };
    }

    private void createProducts(ProductRepository productRepository) {
        productRepository.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"),
                "An Apple portable music player"));
        productRepository.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"),
                "An Apple tablet device"));
        Product
                macbook =
                new Product(3L, "Apple macBook", new BigDecimal("899.99"),
                        "An Apple notebook computer");
        macbook.addAttribute("warranty", "included");
        productRepository.save(macbook);
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        LongStream.rangeClosed(1, 3)
                .forEach(customerId ->
                        customerRepository.save(new Customer(customerId, new EmailAddress(customerId + "@2.com"), "John" + customerId, "Smith" + customerId)));
    }
}
