package examples.springdata.geode.client.transactions.client.service;

import examples.springdata.geode.client.transactions.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public List<Customer> createFiveCustomers() {
        return Arrays.stream(new Customer[]{new Customer(1L, new EmailAddress("1@1.com"), "John", "Melloncamp"),
                new Customer(2L, new EmailAddress("2@2.com"), "Franky", "Hamilton"),
                new Customer(3L, new EmailAddress("3@3.com"), "Sebastian", "Horner"),
                new Customer(4L, new EmailAddress("4@4.com"), "Chris", "Vettel"),
                new Customer(5L, new EmailAddress("5@5.com"), "Kimi", "Rosberg")})
                .map(customerRepository::save)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCustomersSuccess() {
        customerRepository.save(new Customer(2L, new EmailAddress("2@2.com"), "Humpty", "Hamilton"));
    }

    @Transactional
    public void updateCustomersWithDelay(int millisDelay, Customer customer) {
        customerRepository.save(customer);
        try {
            Thread.sleep(millisDelay);
        } catch (InterruptedException e) {
        }
    }

    @Transactional
    public void updateCustomersFailure() {
        customerRepository.save(new Customer(2L, new EmailAddress("2@2.com"), "Numpty", "Hamilton"));
        throw new IllegalArgumentException("This should fail the transactions");
    }
}