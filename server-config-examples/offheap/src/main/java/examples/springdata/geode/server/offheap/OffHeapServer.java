package examples.springdata.geode.server.offheap;

import examples.springdata.geode.server.offheap.config.OffHeapServerConfig;
import examples.springdata.geode.server.offheap.repo.CustomerRepository;
import examples.springdata.geode.server.offheap.repo.ProductRepository;
import examples.springdata.geode.util.DataCreatorsKt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = OffHeapServerConfig.class)
public class OffHeapServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OffHeapServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public ApplicationRunner runner(CustomerRepository customerRepository, ProductRepository productRepository) {
        return args -> {
            DataCreatorsKt.createCustomers(3000, customerRepository);
            DataCreatorsKt.createProducts(1000, productRepository);
        };
    }
}
