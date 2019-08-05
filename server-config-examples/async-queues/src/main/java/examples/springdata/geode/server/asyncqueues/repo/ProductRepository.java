package examples.springdata.geode.server.asyncqueues.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
