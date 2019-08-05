package examples.springdata.geode.client.clusterregion.client.repo;

import org.springframework.data.gemfire.mapping.annotation.ClientRegion;

import examples.springdata.geode.client.common.client.repo.BaseCustomerRepository;

@ClientRegion("Customers")
public interface CustomerRepository extends BaseCustomerRepository {
}
