package org.springframework.data.examples.geode.oql.client;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.oql.client.repo.CustomerRepository;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	@Resource
	private Region<Long, Customer> customerRegion;

	@Resource
	private GemfireTemplate customerTemplate;

	public CustomerService(CustomerRepository customerRepository, GemfireTemplate customerTemplate) {
		this.customerRepository = customerRepository;
		this.customerTemplate = customerTemplate;
	}

	private CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void save(Customer customer) {
		getCustomerRepository().save(customer);
	}

	public Optional<Customer> findById(Long id) {
		return getCustomerRepository().findById(id);
	}

	public List findWithTemplate(String queryString, Object... parameters) {
		return customerTemplate.find(queryString, parameters).asList();
	}

	public List findByEmailAddress(String emailAddress) {
		return customerRepository.findByEmailAddressUsingIndex(emailAddress);
	}

	public List findByFirstNameUsingIndex(String firstName) {
		return customerRepository.findByFirstNameUsingIndex(firstName);
	}
}
