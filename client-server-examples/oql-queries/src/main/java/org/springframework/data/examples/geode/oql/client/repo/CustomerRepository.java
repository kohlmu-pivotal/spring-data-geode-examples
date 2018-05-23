package org.springframework.data.examples.geode.oql.client.repo;

import java.util.List;

import org.springframework.data.examples.geode.common.client.repo.BaseCustomerRepository;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.gemfire.repository.query.annotation.Hint;
import org.springframework.data.gemfire.repository.query.annotation.Limit;
import org.springframework.data.gemfire.repository.query.annotation.Trace;

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