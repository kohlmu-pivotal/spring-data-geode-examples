package example.springdata.geode.server.syncqueues.repo;

import examples.springdata.geode.domain.OrderProductSummary;
import examples.springdata.geode.domain.OrderProductSummaryKey;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductSummaryRepository extends CrudRepository<OrderProductSummary, OrderProductSummaryKey> {
}
