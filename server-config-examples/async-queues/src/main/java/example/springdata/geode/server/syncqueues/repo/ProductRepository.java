package example.springdata.geode.server.syncqueues.repo;

import examples.springdata.geode.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Customer, Long> {
}
