package examples.springdata.geode.client.client.function.client.repo;

import examples.springdata.geode.client.client.common.client.repo.BaseCustomerRepository;
import examples.springdata.geode.client.client.common.client.repo.BaseCustomerRepository;
import examples.springdata.geode.domain.Customer;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.gemfire.repository.query.annotation.Hint;
import org.springframework.data.gemfire.repository.query.annotation.Limit;
import org.springframework.data.gemfire.repository.query.annotation.Trace;

import java.util.List;

@ClientRegion("Customers")
public interface CustomerRepository extends BaseCustomerRepository {

	@Trace
	@Limit(100)
	@Hint("emailAddressIndex")
	@Query("select * from /Customers customer where customer.emailAddress.value = $1")
	List<Customer> findByEmailAddressUsingIndex(String emailAddress);

	@Trace
	@Limit(100)
	@Query("select * from /Customers customer where customer.firstName = $1")
	List<Customer> findByFirstNameUsingIndex(String firstName);
}