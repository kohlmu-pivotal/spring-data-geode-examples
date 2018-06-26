package examples.springdata.geode.cq.client.producer.services;

import examples.springdata.geode.cq.client.producer.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private CustomerRepository customerRepositoryKT;

    public CustomerService(CustomerRepository customerRepositoryKT) {
        this.customerRepositoryKT = customerRepositoryKT;
    }

    public void save(Customer customer) {
        customerRepositoryKT.save(customer);
    }
}
