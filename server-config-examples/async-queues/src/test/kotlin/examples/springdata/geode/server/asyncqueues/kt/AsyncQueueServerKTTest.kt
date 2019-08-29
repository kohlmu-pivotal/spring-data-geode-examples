package examples.springdata.geode.server.asyncqueues.kt

import examples.springdata.geode.domain.*
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
import java.math.BigDecimal
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [AsyncQueueServerKT::class])
class AsyncQueueServerKTTest {
    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    @Resource(name = "Orders")
    private val orders: Region<Long, Order>? = null

    @Resource(name = "Products")
    private val products: Region<Long, Product>? = null

    @Resource(name = "OrderProductSummary")
    private val orderProductSummary: Region<Long, OrderProductSummary>? = null

    @Autowired
    private val customerRepository: CustomerRepositoryKT? = null

    @Autowired
    private val orderRepository: OrderRepositoryKT? = null

    @Autowired
    private val productRepository: ProductRepositoryKT? = null

    @Autowired
    private val orderProductSummaryRepository: OrderProductSummaryRepositoryKT? = null

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers!!.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isNotEmpty
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull
        assertThat(this.orders!!.name).isEqualTo("Orders")
        assertThat(this.orders.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
        assertThat(this.orders).isNotEmpty
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull
        assertThat(this.products!!.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat(this.products).isNotEmpty
    }

    @Test
    fun orderProductSummaryRegionWasConfiguredCorrectly() {

        assertThat(this.orderProductSummary).isNotNull
        assertThat(this.orderProductSummary!!.name).isEqualTo("OrderProductSummary")
        assertThat(this.orderProductSummary.fullPath).isEqualTo(RegionUtils.toRegionPath("OrderProductSummary"))
        assertThat(this.orderProductSummary).isNotEmpty
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {

        val jonDoe = Customer(15L, EmailAddress("example@example.org"), "Jon", "Doe")

        this.customerRepository!!.save(jonDoe)

        assertThat(this.customerRepository.count()).isEqualTo(301)

        val jonOptional = this.customerRepository.findById(15L)

        var jon2: Customer? = null
        if (jonOptional.isPresent) {
            jon2 = jonOptional.get()
        }
        assertThat(jon2).isEqualTo(jonDoe)

        customerRepository.delete(jonDoe)

        assertThat(this.customerRepository.count()).isEqualTo(300)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {
        val product = Product(15L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need")

        this.productRepository!!.save(product)

        assertThat(this.productRepository.count()).isEqualTo(4)

        assertThat(this.productRepository.findById(15L).get()).isEqualTo(product)

        this.productRepository.delete(product)

        assertThat(this.productRepository.count()).isEqualTo(3)
    }

    @Test
    fun orderRepositoryWasAutoConfiguredCorrectly() {

        val order = Order(15L, 1L,
                Address("A", "Seattle", "Canada"),
                Address("B", "San Diego", "Mexico"))

        this.orderRepository!!.save(order)

        assertThat(this.orderRepository.count()).isEqualTo(11)

        assertThat(this.orderRepository.findById(15L).get()).isEqualTo(order)

        orderRepository.delete(order)

        assertThat(this.orderRepository.count()).isEqualTo(10)
    }

    @Test
    fun orderProductSummaryRepositoryWasAutoConfiguredCorrectly() {
        val key = OrderProductSummaryKey(1L, 10L)
        val orderProductSummary = OrderProductSummary(key, BigDecimal(10))

        this.orderProductSummaryRepository!!.save(orderProductSummary)
        assertThat(this.orderProductSummaryRepository.count()).isEqualTo(7)

        assertThat(this.orderProductSummaryRepository.findById(key).get()).isEqualTo(orderProductSummary)

        this.orderProductSummaryRepository.delete(orderProductSummary)
        assertThat(this.orderProductSummaryRepository.count()).isEqualTo(6)
    }
}