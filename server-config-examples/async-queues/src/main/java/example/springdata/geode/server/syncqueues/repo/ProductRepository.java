package example.springdata.geode.server.syncqueues.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
