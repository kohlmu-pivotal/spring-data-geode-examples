package examples.springdata.geode.clusterregion.client.repo;

import java.util.List;

import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

import examples.springdata.geode.domain.Order;

@ClientRegion("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
}
