package examples.springdata.geode.clusterregion.client;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.clusterregion.client.service.CustomerService;
import examples.springdata.geode.entityregion.client.config.ClusterDefinedRegionClientConfig;

@SpringBootApplication(scanBasePackageClasses = ClusterDefinedRegionClientConfig.class)
public class ClusterDefinedRegionClient implements BaseClient {
    public static void main(String[] args) {
        SpringApplication.run(ClusterDefinedRegionClient.class, args);
    }

    @Bean
    public ApplicationRunner runner(CustomerService customerService) {
        return args -> populateData(customerService);
    }
}