package examples.springdata.geode.server.eviction;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.eviction.repo.CustomerRepository;
import examples.springdata.geode.server.eviction.repo.OrderRepository;
import examples.springdata.geode.server.eviction.repo.ProductRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = EvictionServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EvictionServerTest {

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Resource(name = "Orders")
    private Region<Long, Order> orders;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(300);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.productRepository.count()).isEqualTo(300);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        long numOrders = this.orderRepository.count();
        System.out.println("There are " + numOrders + " orders in the Orders region");
        assertThat(numOrders).isEqualTo(10);
    }

    @Test
    public void evictionIsConfiguredCorrectly() {
        assertThat(customers.getAttributes().getEvictionAttributes().getAction().isOverflowToDisk()).isTrue();
        assertThat(products.getAttributes().getEvictionAttributes().getAction().isOverflowToDisk()).isTrue();
        assertThat(orders.getAttributes().getEvictionAttributes().getAction().isLocalDestroy()).isTrue();
    }
}
