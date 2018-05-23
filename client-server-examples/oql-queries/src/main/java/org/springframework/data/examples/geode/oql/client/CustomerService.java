package org.springframework.data.examples.geode.oql.client;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.oql.client.repo.CustomerRepository;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final GemfireTemplate customerTemplate;
	private final ClientCache gemFireCache;

	public CustomerService(CustomerRepository customerRepository, GemfireTemplate customerTemplate,
		ClientCache gemFireCache) {
		this.customerRepository = customerRepository;
		this.customerTemplate = customerTemplate;
		this.gemFireCache = gemFireCache;
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

	public List findByFirstNameLocalClientRegion(String queryString, Object... parameters) {
		Query query = gemFireCache.getLocalQueryService().newQuery(queryString);
		try {
			return ((SelectResults) query.execute(parameters)).asList();
		}
		catch (FunctionDomainException | TypeMismatchException | NameResolutionException | QueryInvocationTargetException e) {
			System.err.println(e);
			return Collections.emptyList();
		}
	}
}
