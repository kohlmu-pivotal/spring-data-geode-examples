package examples.springdata.geode.server.asyncqueues;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.OrderProductSummary;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.asyncqueues.repo.CustomerRepository;
import examples.springdata.geode.server.asyncqueues.repo.OrderProductSummaryRepository;
import examples.springdata.geode.server.asyncqueues.repo.OrderRepository;
import examples.springdata.geode.server.asyncqueues.repo.ProductRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = AsyncQueueServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AsyncQueueServerTest {
    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Resource(name = "Orders")
    private Region<Long, Order> orders;

    @Resource(name = "Products")
    private Region<Long, Product> products;

    @Resource(name = "OrderProductSummary")
    private Region<Long, OrderProductSummary> orderProductSummary;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductSummaryRepository orderProductSummaryRepository;

    @Test
    public void customersRegionWasConfiguredCorrectly() {
        assertThat(this.customers).isNotNull();
        assertThat(this.customers.getName()).isEqualTo("Customers");
        assertThat(this.customers.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Customers"));
        assertThat(this.customers).isNotEmpty();
    }

    @Test
    public void ordersRegionWasConfiguredCorrectly() {
        assertThat(this.orders).isNotNull();
        assertThat(this.orders.getName()).isEqualTo("Orders");
        assertThat(this.orders.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Orders"));
        assertThat(this.orders).isNotEmpty();

        assertThat(this.orders.getAttributes().getAsyncEventQueueIds().size()).isEqualTo(1);
    }

    @Test
    public void productsRegionWasConfiguredCorrectly() {
        assertThat(this.products).isNotNull();
        assertThat(this.products.getName()).isEqualTo("Products");
        assertThat(this.products.getFullPath()).isEqualTo(RegionUtils.toRegionPath("Products"));
        assertThat(this.products).isNotEmpty();
    }

    @Test
    public void orderProductSummaryRegionWasConfiguredCorrectly() {
        assertThat(this.orderProductSummary).isNotNull();
        assertThat(this.orderProductSummary.getName()).isEqualTo("OrderProductSummary");
        assertThat(this.orderProductSummary.getFullPath()).isEqualTo(RegionUtils.toRegionPath("OrderProductSummary"));
        assertThat(this.orderProductSummary).isNotEmpty();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(301);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.productRepository.count()).isEqualTo(3);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.orderRepository.count()).isEqualTo(10);
    }

    @Test
    public void orderProductSummaryRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.orderProductSummaryRepository).isNotNull();
        assertThat(this.orderProductSummaryRepository.count()).isBetween(3L, 6L);
    }
}