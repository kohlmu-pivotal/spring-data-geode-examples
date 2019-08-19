package examples.springdata.geode.client.function.client.services;

import examples.springdata.geode.client.function.client.functions.CustomerFunctionExecutions;
import examples.springdata.geode.client.function.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
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
        return customerFunctionExecutions.listAllCustomersForEmailAddress(emailAddresses).get(0);
    }

    public Customer findById(long customerId) {
        return customerRepository.findById(customerId).get();
    }
}
