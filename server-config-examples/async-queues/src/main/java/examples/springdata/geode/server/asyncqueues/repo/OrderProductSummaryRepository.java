package examples.springdata.geode.server.asyncqueues.repo;

import examples.springdata.geode.domain.OrderProductSummary;
import examples.springdata.geode.domain.OrderProductSummaryKey;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.gemfire.repository.query.annotation.Hint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Region("OrderProductSummary")
public interface OrderProductSummaryRepository extends CrudRepository<OrderProductSummary, OrderProductSummaryKey> {
    @Hint("emailAddressIndex")
    @Query("select orderSummary.value from /OrderProductSummary.entrySet orderSummary where orderSummary.key.productId = $1")
    List<OrderProductSummary> findAllForProductID(long l);
}
