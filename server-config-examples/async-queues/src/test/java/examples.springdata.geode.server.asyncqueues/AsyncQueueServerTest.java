package examples.springdata.geode.server.asyncqueues;

import examples.springdata.geode.domain.*;
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
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = AsyncQueueServer.class)
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

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerRepository.save(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(301);

        Optional<Customer> jonOptional = this.customerRepository.findById(15L);

        Customer jon2 = null;
        if (jonOptional.isPresent()) {
            jon2 = jonOptional.get();
        }
        assertThat(jon2).isEqualTo(jonDoe);

        customerRepository.delete(jonDoe);

        assertThat(this.customerRepository.count()).isEqualTo(300);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(15L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productRepository.save(product);

        assertThat(this.productRepository.count()).isEqualTo(4);

        assertThat(this.productRepository.findById(15L).get()).isEqualTo(product);

        this.productRepository.delete(product);

        assertThat(this.productRepository.count()).isEqualTo(3);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {

        Order order = new Order(15L, 1L,
                new Address("A", "Seattle", "Canada"),
                new Address("B", "San Diego", "Mexico"));

        this.orderRepository.save(order);

        assertThat(this.orderRepository.count()).isEqualTo(11);

        assertThat(this.orderRepository.findById(15L).get()).isEqualTo(order);

        orderRepository.delete(order);

        assertThat(this.orderRepository.count()).isEqualTo(10);
    }

    @Test
    public void orderProductSummaryRepositoryWasAutoConfiguredCorrectly() {
        OrderProductSummaryKey key = new OrderProductSummaryKey(1L, 10L);
        OrderProductSummary orderProductSummary = new OrderProductSummary(key, new BigDecimal(10));

        this.orderProductSummaryRepository.save(orderProductSummary);

        assertThat(this.orderProductSummaryRepository.findById(key).get()).isEqualTo(orderProductSummary);

        this.orderProductSummaryRepository.delete(orderProductSummary);
    }
}