package examples.springdata.geode.client.transactions.client.repo;

import examples.springdata.geode.domain.Product;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ClientRegion("Products")
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
}
