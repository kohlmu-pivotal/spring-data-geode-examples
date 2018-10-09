package example.springdata.geode.client.transactions.client.repo;

import examples.springdata.geode.client.common.client.repo.BaseCustomerRepository;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;

@ClientRegion("Customers")
public interface CustomerRepository extends BaseCustomerRepository {
}
