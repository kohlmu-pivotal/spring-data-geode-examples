package examples.springdata.geode.client.basic;

import examples.springdata.geode.client.basic.config.BasicClientApplicationConfig;
import examples.springdata.geode.client.basic.services.CustomerService;
import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Creates a client to demonstrate basic CRUD operations. This client can be configured in 2 ways,
 * depending on profile selected. "proxy" profile will create a region with PROXY configuration that
 * will store no data locally. "localCache" will create a region that stores data in the local
 * client, to satisfy the "near cache" paradigm.
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = BasicClientApplicationConfig.class)
public class BasicClient implements BaseClient {
    public static void main(String[] args) {
        SpringApplication.run(BasicClient.class, args);
    }

    @Bean
    ApplicationRunner runner(CustomerService customerService) {
        return args -> populateData(customerService);
    }
}
