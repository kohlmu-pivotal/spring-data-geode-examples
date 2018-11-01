package examples.springdata.geode.server.eventhandlers.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.repository.CrudRepository;

@Region("Products")
public interface ProductRepository extends CrudRepository<Product, Long> {
}
