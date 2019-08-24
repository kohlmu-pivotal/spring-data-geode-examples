package examples.springdata.geode.server.wan.server.siteA;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import examples.springdata.geode.server.wan.client.repo.CustomerRepository;
import examples.springdata.geode.server.wan.server.siteA.config.WanEnableServerSiteAConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.stream.LongStream;

@SpringBootApplication(scanBasePackageClasses = WanEnableServerSiteAConfig.class)
public class WanEnabledServerSiteA {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WanEnabledServerSiteA.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

    @Bean
    public ApplicationRunner siteARunner(CustomerRepository customerRepository) {
        return args -> createCustomerData(customerRepository);
    }

    private void createCustomerData(CustomerRepository customerRepository) {
        System.out.println("Inserting 300 entries");
        Faker faker = new Faker();
        LongStream.rangeClosed(0, 300)
                .forEach(customerId ->
                        customerRepository.save(
                                new Customer(customerId,
                                        new EmailAddress(faker.internet().emailAddress()), faker.name().firstName(), faker.name().lastName())));
    }
}
