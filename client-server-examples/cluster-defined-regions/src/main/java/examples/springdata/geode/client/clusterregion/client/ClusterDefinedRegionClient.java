package examples.springdata.geode.client.clusterregion.client;

import examples.springdata.geode.client.clusterregion.client.config.ClusterDefinedRegionClientConfig;
import examples.springdata.geode.client.clusterregion.client.service.CustomerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.client.common.client.BaseClient;

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