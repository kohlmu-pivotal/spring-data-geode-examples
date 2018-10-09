package example.springdata.geode.client.transactions.client;

import example.springdata.geode.client.transactions.client.config.TransactionalClientConfig;
import example.springdata.geode.client.transactions.client.service.CustomerService;
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
            customerService.createFiveCustomers();
            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }
}
