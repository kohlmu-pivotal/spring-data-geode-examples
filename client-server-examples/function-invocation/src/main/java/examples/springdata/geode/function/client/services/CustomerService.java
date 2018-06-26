package examples.springdata.geode.function.client.services;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.function.client.functions.CustomerFunctionExecutions;
import examples.springdata.geode.function.client.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
