package examples.springdata.geode.functions.cascading.client.services;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.functions.cascading.client.functions.CustomerFunctionExecutions;
import examples.springdata.geode.functions.cascading.client.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerFunctionExecutions customerFunctionExecutions;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Long> listAllCustomers() {
        return customerFunctionExecutions.listAllCustomers().get(0);
    }
}