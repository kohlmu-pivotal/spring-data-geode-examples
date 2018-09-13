package examples.springdata.geode.client.entityregion;

import examples.springdata.geode.client.entityregion.config.EntityDefinedRegionClientConfig;
import examples.springdata.geode.client.entityregion.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedRegionClientConfig.class)
public class EntityDefinedRegionClient {
    public static void main(String[] args) {
        SpringApplication.run(EntityDefinedRegionClient.class, args);
    }

    @Bean
    public ApplicationRunner runner(CustomerService customerService) {
        return args -> {
            System.out.println("Inserting 3 entries for keys: 1, 2, 3");
            customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
            customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
            customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));

            System.out.println("Entries on Client: " + customerService.numberEntriesStoredLocally());
            System.out.println("Entries on Server: " + customerService.numberEntriesStoredOnServer());
            customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));

            System.out.println("Updating entry for key: 2");
            System.out.println("Entry Before: " + customerService.findById(2L).get());
            customerService.save(new Customer(2L, new EmailAddress("4@4.com"), "Sam", "Spacey"));
            System.out.println("Entry After: " + customerService.findById(2L).get());

            System.out.println("Removing entry for key: 3");
            customerService.deleteById(3L);

            System.out.println("Entries:");
            customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
        };
    }
}