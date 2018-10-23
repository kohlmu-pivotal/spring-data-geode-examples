package example.springdata.geode.client.transactions.client;

import example.springdata.geode.client.transactions.client.config.TransactionalClientConfig;
import example.springdata.geode.client.transactions.client.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = TransactionalClientConfig.class)
public class TransactionalClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TransactionalClient.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

    @Bean
    ApplicationRunner runner(CustomerService customerService) {
        return args -> {
            System.out.println("Number of Entries stored before = " + customerService.numberEntriesStoredOnServer());
            customerService.createFiveCustomers();
            System.out.println("Number of Entries stored after = " + customerService.numberEntriesStoredOnServer());
            System.out.println("Customer for ID before (transaction commit success) = " + customerService.findById(2L));
            customerService.updateCustomersSuccess();
            System.out.println("Customer for ID after (transaction commit success) = " + customerService.findById(2L));
            try {
                customerService.updateCustomersFailure();
            } catch (IllegalArgumentException exception) {
            }
            System.out.println("Customer for ID after (transaction commit failure) = " + customerService.findById(2L));

            customerService.updateCustomersWithDelay(1000, new Customer(2L, new EmailAddress("2@2.com"), "Numpty", "Hamilton"));
            customerService.updateCustomersWithDelay(10, new Customer(2L, new EmailAddress("2@2.com"), "Frumpy", "Hamilton"));
            System.out.println("Customer for ID after 2 updates with delay = " + customerService.findById(2L));
            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }
}
