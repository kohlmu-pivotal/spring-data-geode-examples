package org.springframework.data.examples.geode.function.client.repo;

import org.springframework.data.examples.geode.model.Order;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {

}
