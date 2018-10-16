package examples.springdata.geode.server.kt.lucene.repo;

import examples.springdata.geode.server.kt.lucene.domain.LuceneCustomer;
import org.springframework.data.repository.CrudRepository;

public interface LuceneCustomerRepo extends CrudRepository<LuceneCustomer, Long> {
}
