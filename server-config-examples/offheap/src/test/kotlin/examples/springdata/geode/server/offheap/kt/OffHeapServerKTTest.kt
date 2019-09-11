package examples.springdata.geode.server.offheap.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.offheap.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.offheap.kt.repo.ProductRepositoryKT
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [OffHeapServerKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OffHeapServerKTTest {

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Resource(name = "Products")
    lateinit var products: Region<Long, Product>

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Autowired
    lateinit var productRepository: ProductRepositoryKT

    @Test
    fun customerRepositoryIsAutoConfiguredCorrectly() {
        assertThat(customerRepository.count()).isEqualTo(3000)
    }

    @Test
    fun productRepositoryIsAutoConfiguredCorrectly() {
        assertThat(productRepository.count()).isEqualTo(1000)
    }

    @Test
    fun offHeapConfiguredCorrectly() {
        assertThat(customers.attributes.offHeap).isTrue()
        assertThat(products.attributes.offHeap).isTrue()

        println("Entries in 'Customers' region are stored " + (if (customers.attributes.offHeap) "OFF" else "ON") + " heap")
        println("Entries in 'Products' region are stored " + (if (products.attributes.offHeap) "OFF" else "ON") + " heap")
    }
}