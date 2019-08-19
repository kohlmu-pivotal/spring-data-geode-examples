package examples.springdata.geode.client.entityregion.client;

import examples.springdata.geode.client.entityregion.client.service.CustomerService;
import examples.springdata.geode.client.entityregion.client.service.OrderService;
import examples.springdata.geode.client.entityregion.client.service.ProductService;
import examples.springdata.geode.client.entityregion.server.EntityDefinedRegionServer;
import examples.springdata.geode.domain.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport.startGemFireServer;

@ActiveProfiles({"test", "default"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EntityDefinedRegionClientTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @BeforeClass
    public static void setup() throws IOException {
        startGemFireServer(EntityDefinedRegionServer.class);
    }

    @Test
    public void customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull();
    }

    @Test
    public void orderServiceWasConfiguredCorrectly() {

        assertThat(this.orderService).isNotNull();
    }

    @Test
    public void productServiceWasConfiguredCorrectly() {

        assertThat(this.productService).isNotNull();
    }

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {

        Customer jonDoe = new Customer(15L, new EmailAddress("example@example.org"), "Jon", "Doe");

        this.customerService.save(jonDoe);

        assertThat(this.customerService.numberEntriesStoredOnServer()).isEqualTo(3);

        Optional<Customer> jonOptional = this.customerService.findById(15);

        Customer jon2 = null;
        if(jonOptional.isPresent()) {
            jon2 = jonOptional.get();
        }

        assertThat(jon2).isEqualTo(jonDoe);
    }

    @Test
    public void orderRepositoryWasAutoConfiguredCorrectly() {
        Address address = new Address("Sesame ", "London", "Japan");
        Order order = new Order(1L, 1L, address, address);

        this.orderService.save(order);

        assertThat(this.orderService.findAll().size()).isEqualTo(1);

        assertThat(this.orderService.findById(1L)).isEqualTo(order);
    }

    @Test
    public void productRepositoryWasAutoConfiguredCorrectly() {
        Product product = new Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need");

        this.productService.save(product);

        assertThat(this.productService.findAll().size()).isEqualTo(1);

        assertThat(this.productService.findById(1L)).isEqualTo(product);
    }
}