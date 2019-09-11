package examples.springdata.geode.server.offheap;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.offheap.repo.CustomerRepository;
import examples.springdata.geode.server.offheap.repo.ProductRepository;
import org.apache.geode.cache.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = OffHeapServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OffHeapServerTest {

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void customerRepositoryIsAutoConfiguredCorrectly() {
        assertThat(customerRepository.count()).isEqualTo(3000);
    }

    @Test
    public void productRepositoryIsAutoConfiguredCorrectly() {
        assertThat(productRepository.count()).isEqualTo(1000);
    }

    @Test
    public void offHeapConfiguredCorrectly() {
        assertThat(customers.getAttributes().getOffHeap()).isTrue();
        assertThat(products.getAttributes().getOffHeap()).isTrue();

        System.out.println("Entries in 'Customers' region are stored " + (customers.getAttributes().getOffHeap()? "OFF": "ON") + " heap");
        System.out.println("Entries in 'Products' region are stored " + (products.getAttributes().getOffHeap()? "OFF": "ON") + " heap");
    }
}