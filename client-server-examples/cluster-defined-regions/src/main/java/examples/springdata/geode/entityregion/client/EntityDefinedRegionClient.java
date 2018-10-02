package examples.springdata.geode.entityregion.client;

import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.entityregion.client.config.EntityDefinedRegionClientConfig;
import examples.springdata.geode.entityregion.client.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedRegionClientConfig.class)
public class EntityDefinedRegionClient implements BaseClient {
    public static void main(String[] args) {
        SpringApplication.run(EntityDefinedRegionClient.class, args);
    }

    @Bean
    public ApplicationRunner runner(CustomerService customerService) {
        return args -> populateData(customerService);
    }
}