package examples.springdata.geode.client.security.client.services;

import examples.springdata.geode.client.security.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}