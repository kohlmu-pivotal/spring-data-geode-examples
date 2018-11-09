package example.springdata.geode.server.wan;

import com.github.javafaker.Faker;
import example.springdata.geode.server.wan.config.WanEnableServerConfig;
import example.springdata.geode.server.wan.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Scanner;
import java.util.stream.LongStream;

@SpringBootApplication(scanBasePackageClasses = WanEnableServerConfig.class)
public class WanEnableServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WanEnableServer.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

    @Bean
    @Profile({"default", "SiteA"})
    public ApplicationRunner siteARunner(CustomerRepository customerRepository) {
        return args -> createCustomerData(customerRepository);
    }

    @Bean
    @Profile("SiteB")
    public ApplicationRunner siteBRunner() {
        return args -> new Scanner(System.in).nextLine();
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 3 entries for keys: 1, 2, 3");
        Faker faker = new Faker();
        LongStream.rangeClosed(0, 300)
                .parallel()
                .forEach(customerId ->
                        customerRepository.save(
                                new Customer(customerId,
                                        new EmailAddress(faker.internet().emailAddress()), faker.name().firstName(), faker.name().lastName())));
    }
}
