package org.springframework.data.examples.geode.basic.client;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.examples.geode.basic.repository.CustomerRepository;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


	private final CustomerRepository customerRepository;

	@Resource @Qualifier(BasicClientApplicationConfig.CUSTOMER_REGION_BEAN_NAME)
	private Region<Long, Customer> customerRegion;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	private CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void save(Customer customer) {
		getCustomerRepository().save(customer);
	}

	public List<Customer> findAll() {
		return getCustomerRepository().findAll();
	}

	public Optional<Customer> findById(Long id) {
		return getCustomerRepository().findById(id);
	}

	public int numberEntriesStoredLocally() {
		return customerRegion.size();
	}

	public int numberEntriesStoredOnServer() {
		return customerRegion.keySetOnServer().size();
	}

	public void deleteById(Long id) {
		getCustomerRepository().deleteById(id);
	}
}