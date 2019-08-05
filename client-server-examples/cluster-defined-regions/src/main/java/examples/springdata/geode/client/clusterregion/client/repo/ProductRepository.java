package examples.springdata.geode.client.clusterregion.client.repo;

import java.util.List;

import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

import examples.springdata.geode.domain.Product;

@ClientRegion("Products")
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
}
