package examples.springdata.geode.server.expiration.custom;

import com.github.javafaker.Faker;
import com.jayway.awaitility.Awaitility;
import examples.springdata.geode.domain.Address;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import examples.springdata.geode.server.expiration.custom.repo.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CustomExpiryServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CustomExpiryServerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    Faker faker;

    @Test
    public void expirationIsConfiguredCorrectly() {
        customerRepository.save(new Customer(3L, new EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                new Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())));

        assertThat(customerRepository.count()).isEqualTo(1);

        final Callable<Boolean> conditionEvaluator = () -> !customerRepository.findById(3L).isPresent();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS");
        System.out.println("Starting TTL wait period: " + simpleDateFormat.format(new Date()));
        //Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
        // will be used.
        Awaitility.await()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until(conditionEvaluator);

        assertThat(customerRepository.count()).isEqualTo(0);

        System.out.println("Ending TTL wait period: " + simpleDateFormat.format(new Date()));

        customerRepository.save(new Customer(3L, new EmailAddress(faker.internet().emailAddress()),
                faker.name().firstName(), faker.name().lastName(),
                new Address(faker.address().streetAddress(), faker.address().city(), faker.address().country())));

        assertThat(customerRepository.count()).isEqualTo(1);

        System.out.println("Starting Idle wait period: " + simpleDateFormat.format(new Date()));

        //Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
        Awaitility.await()
                .pollDelay(2, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .until(conditionEvaluator);

        assertThat(customerRepository.count()).isEqualTo(0);

        System.out.println("Ending Idle wait period: " + simpleDateFormat.format(new Date()));
    }
}