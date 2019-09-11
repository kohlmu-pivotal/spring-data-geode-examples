package examples.springdata.geode.server.expiration.custom.repo;

import examples.springdata.geode.domain.Customer;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.repository.CrudRepository;

@Region("Customers")
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
