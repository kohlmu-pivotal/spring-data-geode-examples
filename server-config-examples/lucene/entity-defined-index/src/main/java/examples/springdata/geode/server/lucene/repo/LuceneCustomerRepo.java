package examples.springdata.geode.server.lucene.repo;

import examples.springdata.geode.server.lucene.domain.JavaLuceneCustomer;
import org.springframework.data.repository.CrudRepository;

public interface LuceneCustomerRepo extends CrudRepository<JavaLuceneCustomer, Long> {
}
