package examples.springdata.geode.client.basic.services;

import examples.springdata.geode.client.basic.repo.CustomerRepository;
import org.apache.geode.cache.Region;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;

	@Resource(name = "Customers")
	private Region<Long, Customer> customerRegion;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		this.customerRegion = customerRegion;
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
