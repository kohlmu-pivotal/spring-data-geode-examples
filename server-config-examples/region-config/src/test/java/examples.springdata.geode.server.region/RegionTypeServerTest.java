package examples.springdata.geode.server.region;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.region.repo.CustomerRepository;
import examples.springdata.geode.server.region.repo.OrderRepository;
import examples.springdata.geode.server.region.repo.ProductRepository;
import org.apache.geode.cache.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.util.RegionUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RegionTypeServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegionTypeServerTest {
    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Orders")
    private Region<Long, Order> orders;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isNotEmpty();
        assertThat(this.customers.getAttributes().getPartitionAttributes().getTotalNumBuckets()).isEqualTo(13);
        assertThat(this.customers.getAttributes().getPartitionAttributes().getRedundantCopies()).isEqualTo(1);
    }

    @Test
    public void ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull();
        assertThat(this.orders.getName()).isEqualTo("Orders");
        assertThat(this.orders.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Orders"));
        assertThat(this.orders).isNotEmpty();
    }

    @Test
    public void productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull();
        assertThat(this.products.getName()).isEqualTo("Products");
        assertThat(this.products.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Products"));
        assertThat(this.products).isNotEmpty();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.customerRepository.count()).isEqualTo(3001);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.productRepository.count()).isEqualTo(3);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.orderRepository.count()).isEqualTo(100);
    }
}