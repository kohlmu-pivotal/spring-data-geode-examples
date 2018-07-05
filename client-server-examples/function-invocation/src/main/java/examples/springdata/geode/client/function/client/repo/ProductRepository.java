package examples.springdata.geode.client.function.client.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("Products")
public interface ProductRepository extends CrudRepository<Product, Long> {

}
