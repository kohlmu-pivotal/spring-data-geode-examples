package examples.springdata.geode.server.compression.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.compression.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.Region
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CompressionEnabledServerKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CompressionEnabledServerKTTest {

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(4000)
    }

    @Test
    fun compressionIsWorkingCorrectly() {
        assertThat(customers.attributes.compressor).isNotNull
    }
}