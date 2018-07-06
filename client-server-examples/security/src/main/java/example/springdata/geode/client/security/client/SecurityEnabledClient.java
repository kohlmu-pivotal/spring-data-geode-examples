package example.springdata.geode.client.security.client;

import example.springdata.geode.client.security.client.config.SecurityEnabledClientConfiguration;
import example.springdata.geode.client.security.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledClientConfiguration.class)
public class SecurityEnabledClient {
    public final CustomerRepository customerRepository;

    public SecurityEnabledClient(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SecurityEnabledClient.class, args);
        SecurityEnabledClient securityEnabledClient = applicationContext.getBean(SecurityEnabledClient.class);
        securityEnabledClient.customerRepository.save(
                new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
    }
}