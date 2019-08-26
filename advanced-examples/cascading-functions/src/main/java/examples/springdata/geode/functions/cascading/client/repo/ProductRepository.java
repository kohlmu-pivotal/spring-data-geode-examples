package examples.springdata.geode.functions.cascading.client.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
