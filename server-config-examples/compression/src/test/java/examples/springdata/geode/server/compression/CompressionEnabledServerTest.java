package examples.springdata.geode.server.compression;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.compression.repo.CustomerRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CompressionEnabledServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CompressionEnabledServerTest {

    @Resource(name = "Customers")
    private Region<Long, Customer> customers;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(4000);
    }

    @Test
    public void compressionIsWorkingCorrectly() {
        assertThat(customers.getAttributes().getCompressor()).isNotNull();
    }
}