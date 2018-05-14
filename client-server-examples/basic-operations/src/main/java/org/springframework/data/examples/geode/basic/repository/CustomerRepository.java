package org.springframework.data.examples.geode.basic.repository;

import java.util.List;

import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.domain.EmailAddress;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

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