package example.springdata.geode.client.transactions.client.repo;

import examples.springdata.geode.domain.Order;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ClientRegion("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
}
