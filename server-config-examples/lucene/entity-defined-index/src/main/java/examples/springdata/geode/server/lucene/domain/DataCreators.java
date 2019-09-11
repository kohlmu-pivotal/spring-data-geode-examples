package examples.springdata.geode.server.lucene.domain;

import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import com.github.javafaker.Name;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.data.repository.CrudRepository;

public class DataCreators {
    public static void createLuceneCustomers(int numberOfCustomer, CrudRepository<Customer, Long> repository) {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        Internet fakerInternet = faker.internet();
        for (int i = 0; i < numberOfCustomer; i++) {
            repository.save(new Customer(i, new EmailAddress(fakerInternet.emailAddress()), fakerName.firstName(), fakerName.lastName()));
        }
    }
}
