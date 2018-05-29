package org.springframework.data.examples.geode.function.client.repo;

import org.springframework.data.examples.geode.model.Product;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("Products")
public interface ProductRepository extends CrudRepository<Product, Long> {

}
