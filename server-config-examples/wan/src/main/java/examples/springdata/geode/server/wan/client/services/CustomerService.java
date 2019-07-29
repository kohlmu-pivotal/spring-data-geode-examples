package examples.springdata.geode.server.wan.client.services;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.wan.server.repo.CustomerRepository;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements examples.springdata.geode.client.common.client.service.CustomerService {
    private final CustomerRepository customerRepository;

    @Resource(name = "Customers")
    private Region<Long, Customer> customerRegion;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    @Override
    public void save(Customer customer) {
        getCustomerRepository().save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return (List<Customer>) getCustomerRepository().findAll();
    }

    @Override
    public Optional<Customer> findById(long id) {
        return getCustomerRepository().findById(id);
    }

    @Override
    public int numberEntriesStoredLocally() {
        return customerRegion.size();
    }

    @Override
    public int numberEntriesStoredOnServer() {
        return customerRegion.keySetOnServer().size();
    }

    @Override
    public void deleteById(long id) {
        getCustomerRepository().deleteById(id);
    }
}
