package examples.springdata.geode.client.entityregion.client;

import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.client.entityregion.client.config.EntityDefinedRegionClientConfig;
import examples.springdata.geode.client.entityregion.client.service.CustomerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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