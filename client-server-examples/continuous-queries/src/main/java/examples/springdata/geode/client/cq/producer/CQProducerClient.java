package examples.springdata.geode.client.cq.producer;

import examples.springdata.geode.client.cq.producer.config.CQProducerClientApplicationConfig;
import examples.springdata.geode.client.cq.producer.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = CQProducerClientApplicationConfig.class)
public class CQProducerClient {
    public static void main(String[] args) {
        SpringApplication.run(CQProducerClient.class, args);
    }

    private static void insertData(CustomerService customerService, Customer customer) {
        customerService.save(customer);
        System.out.println("Inserted customer = " + customer);
    }

    @Bean
    ApplicationRunner runner(CustomerService customerService) {
        return args -> {
            System.out.println("Inserting 3 entries for keys: 1, 2, 3");
            insertData(customerService, new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
            insertData(customerService,
                    new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
            insertData(customerService, new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));
        };
    }
}
