package org.springframework.data.examples.geode.function.client.services;

import java.util.List;

import org.springframework.data.examples.geode.function.client.functions.CustomerFunctionExecutions;
import org.springframework.data.examples.geode.function.client.repo.CustomerRepository;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerFunctionExecutions customerFunctionExecutions;

	public CustomerService(CustomerRepository customerRepository,
		CustomerFunctionExecutions customerFunctionExecutions) {
		this.customerRepository = customerRepository;
		this.customerFunctionExecutions = customerFunctionExecutions;
	}

	private CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void save(Customer customer) {
		getCustomerRepository().save(customer);
	}

	public List<Customer> listAllCustomersForEmailAddress(String... emailAddresses) {
		return customerFunctionExecutions.listAllCustomersForEmailAddress(emailAddresses);
	}

	public Customer findById(long customerId) {
		return customerRepository.findById(customerId).get();
	}
}
