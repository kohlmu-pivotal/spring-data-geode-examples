package examples.springdata.geode.server.lucene.repo;

import examples.springdata.geode.server.lucene.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
