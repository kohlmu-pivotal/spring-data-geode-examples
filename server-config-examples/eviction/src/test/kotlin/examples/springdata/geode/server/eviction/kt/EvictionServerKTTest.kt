package examples.springdata.geode.server.eviction.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.eviction.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.eviction.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.eviction.kt.repo.ProductRepositoryKT
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [EvictionServerKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EvictionServerKTTest {

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Resource(name = "Products")
    lateinit var products: Region<Long, Product>

    @Resource(name = "Orders")
    lateinit var orders: Region<Long, Order>

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Autowired
    lateinit var productRepository: ProductRepositoryKT

    @Autowired
    lateinit var orderRepository: OrderRepositoryKT

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(300)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.productRepository.count()).isEqualTo(300)
    }

    @Test
    fun orderRepositoryWasAutoConfiguredCorrectly() {
        val numOrders = this.orderRepository.count()
        println("There are $numOrders orders in the Orders region")
        assertThat(numOrders).isEqualTo(10)
    }

    @Test
    fun evictionIsConfiguredCorrectly() {
        assertThat(customers.attributes.evictionAttributes.action.isOverflowToDisk).isTrue()
        assertThat(products.attributes.evictionAttributes.action.isOverflowToDisk).isTrue()
        assertThat(orders.attributes.evictionAttributes.action.isLocalDestroy).isTrue()
    }
}