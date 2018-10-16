package examples.springdata.geode.expiration.cache;

import com.github.javafaker.Faker;
import com.jayway.awaitility.Awaitility;
import examples.springdata.geode.domain.Address;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import examples.springdata.geode.expiration.cache.config.CacheDefinedExpirationServerConfig;
import examples.springdata.geode.expiration.cache.repo.CustomerRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackageClasses = CacheDefinedExpirationServerConfig.class)
public class CacheDefinedExpirationServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheDefinedExpirationServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public Faker createDataFaker() {
        return new Faker();
    }

    @Bean
    public ApplicationRunner runner(CustomerRepository customerRepository, Faker faker) {
        return args -> {
            customerRepository.save(new Customer(1L, new EmailAddress(faker.internet().emailAddress()),
                    faker.name().firstName(), faker.name().lastName(),
                    new Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())));

            final Callable<Boolean> conditionEvaluator = () -> !customerRepository.findById(1L).isPresent();


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS");
            System.out.println("Starting TTL wait period: " + simpleDateFormat.format(new Date()));
            //Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
            // will be used.
            Awaitility.await()
                    .pollInterval(1, TimeUnit.SECONDS)
                    .atMost(10, TimeUnit.SECONDS)
                    .until(conditionEvaluator);

            System.out.println("Ending TTL wait period: " + simpleDateFormat.format(new Date()));

            customerRepository.save(new Customer(1L, new EmailAddress(faker.internet().emailAddress()),
                    faker.name().firstName(), faker.name().lastName(),
                    new Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())));

            System.out.println("Starting Idle wait period: " + simpleDateFormat.format(new Date()));

            //Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
            Awaitility.await()
                    .pollDelay(2, TimeUnit.SECONDS)
                    .pollInterval(100, TimeUnit.MILLISECONDS)
                    .atMost(10, TimeUnit.SECONDS)
                    .until(conditionEvaluator);

            System.out.println("Ending Idle wait period: " + simpleDateFormat.format(new Date()));
        };
    }
}
