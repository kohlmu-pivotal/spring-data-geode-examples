package examples.springdata.geode.function.client.repo;

import examples.springdata.geode.domain.Order;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {

}
