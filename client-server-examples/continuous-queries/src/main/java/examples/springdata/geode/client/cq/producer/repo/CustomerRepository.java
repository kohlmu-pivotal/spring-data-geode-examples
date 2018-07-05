package examples.springdata.geode.client.cq.producer.repo;

import examples.springdata.geode.domain.Customer;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion(name = "Customers")
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
