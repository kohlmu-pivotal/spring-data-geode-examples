package org.springframework.data.examples.geode.common.client.repo;

import java.util.List;

import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.model.EmailAddress;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

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