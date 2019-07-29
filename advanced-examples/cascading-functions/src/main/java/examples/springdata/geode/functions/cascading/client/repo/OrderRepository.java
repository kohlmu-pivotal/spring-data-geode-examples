package examples.springdata.geode.functions.cascading.client.repo;

import examples.springdata.geode.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
