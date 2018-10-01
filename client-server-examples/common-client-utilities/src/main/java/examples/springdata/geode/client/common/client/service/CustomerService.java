package examples.springdata.geode.client.common.client.service;

import java.util.Optional;

import examples.springdata.geode.domain.Customer;

public interface CustomerService {

	int numberEntriesStoredLocally();

	int numberEntriesStoredOnServer();

	void save(Customer customer);

	Iterable<Customer> findAll();

	Optional<Customer> findById(long id);

	void deleteById(long id);
}
