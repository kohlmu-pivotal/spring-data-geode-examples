package example.springdata.geode.server.region.repo;

import examples.springdata.geode.domain.Order;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.repository.CrudRepository;

@Region("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {
}
