package examples.springdata.geode.client.serialization.client.services;

import examples.springdata.geode.client.serialization.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.Region;
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

	public Optional<Customer> findById(long id) {
		return getCustomerRepository().findById(id);
	}

	public int numberEntriesStoredLocally() {
		return customerRegion.size();
	}

	public int numberEntriesStoredOnServer() {
		return customerRegion.keySetOnServer().size();
	}

	public void deleteById(long id) {
		getCustomerRepository().deleteById(id);
	}
}
