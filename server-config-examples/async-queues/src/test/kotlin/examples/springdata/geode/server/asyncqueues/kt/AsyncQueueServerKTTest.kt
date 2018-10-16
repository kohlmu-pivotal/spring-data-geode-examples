package examples.springdata.geode.server.asyncqueues.kt

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.OrderProductSummary
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.asyncqueues.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.OrderProductSummaryRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.ProductRepositoryKT
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [AsyncQueueServerKT::class])
class AsyncQueueServerKTTest {
    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Resource(name = "Orders")
    lateinit var orders: Region<Long, Order>

    @Resource(name = "Products")
    lateinit var products: Region<Long, Product>

    @Resource(name = "OrderProductSummary")
    lateinit var orderProductSummary: Region<Long, OrderProductSummary>

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Autowired
    lateinit var orderRepository: OrderRepositoryKT

    @Autowired
    lateinit var productRepository: ProductRepositoryKT

    @Autowired
    lateinit var orderProductSummaryRepository: OrderProductSummaryRepositoryKT

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isNotEmpty
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull
        assertThat(this.orders.name).isEqualTo("Orders")
        assertThat(this.orders.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
        assertThat(this.orders).isNotEmpty

        assertThat(this.orders.attributes.asyncEventQueueIds.size).isEqualTo(1)
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull
        assertThat(this.products.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat(this.products).isNotEmpty
    }

    @Test
    fun orderProductSummaryRegionWasConfiguredCorrectly() {

        assertThat(this.orderProductSummary).isNotNull
        assertThat(this.orderProductSummary.name).isEqualTo("OrderProductSummary")
        assertThat(this.orderProductSummary.fullPath).isEqualTo(RegionUtils.toRegionPath("OrderProductSummary"))
        assertThat(this.orderProductSummary).isNotEmpty
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.customerRepository.count()).isEqualTo(301)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {
        assertThat(this.productRepository.count()).isEqualTo(3)
    }

    @Test
    fun orderRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.orderRepository.count()).isEqualTo(10)
    }

    @Test
    fun orderProductSummaryRepositoryWasAutoConfiguredCorrectly() {

        assertThat(this.orderProductSummaryRepository.count()).isBetween(3L, 6L)
    }
}