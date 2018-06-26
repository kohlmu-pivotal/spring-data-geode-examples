package examples.springdata.geode.common.client.repo;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ClientRegion(name = "Customers")
public interface BaseCustomerRepository extends CrudRepository<Customer, Long> {

	/**
	 * Returns all [Customer]s.
	 *
	 * @return
	 */
	List<Customer> findAll();

	/**
	 * Finds all [Customer]s with the given lastname.
	 *
	 * @param lastName
	 * @return
	 */
	List<Customer> findByLastName(String lastName);

	/**
	 * Finds the Customer with the given [EmailAddress].
	 *
	 * @param emailAddress
	 * @return
	 */
	Customer findByEmailAddress(EmailAddress emailAddress);
}