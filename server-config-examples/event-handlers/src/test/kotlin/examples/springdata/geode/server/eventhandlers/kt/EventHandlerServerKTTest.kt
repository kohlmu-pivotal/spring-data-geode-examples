package examples.springdata.geode.server.eventhandlers.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.eventhandlers.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.eventhandlers.kt.repo.ProductRepositoryKT
import org.apache.geode.cache.Region
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.util.RegionUtils
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [EventHandlerServerKT::class])
class EventHandlerServerKTTest {
    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    @Resource(name = "Products")
    private val products: Region<Long, Product>? = null

    @Autowired
    internal var customerRepository: CustomerRepositoryKT? = null

    @Autowired
    internal var productRepository: ProductRepositoryKT? = null

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers!!.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isNotEmpty
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull
        assertThat(this.products!!.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat(this.products).isNotEmpty
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.customerRepository!!.count()).isEqualTo(3)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.productRepository!!.count()).isEqualTo(4)
    }
}