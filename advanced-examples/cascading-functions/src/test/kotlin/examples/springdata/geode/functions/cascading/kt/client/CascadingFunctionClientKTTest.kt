package examples.springdata.geode.server.wan.functions.cascading.kt.client

import examples.springdata.geode.domain.*
import examples.springdata.geode.functions.cascading.kt.client.CascadingFunctionClientKT
import examples.springdata.geode.functions.cascading.kt.client.services.CustomerServiceKT
import examples.springdata.geode.functions.cascading.kt.client.services.OrderServiceKT
import examples.springdata.geode.functions.cascading.kt.client.services.ProductServiceKT
import examples.springdata.geode.functions.cascading.kt.server.CascadingFunctionServerKT
import org.apache.geode.cache.Region
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport.startGemFireServer
import org.springframework.data.gemfire.util.RegionUtils
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import javax.annotation.Resource

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CascadingFunctionClientKT::class])
class CascadingFunctoinClientKTTest {
    @Autowired
    private val customerService: CustomerServiceKT? = null

    @Autowired
    private val orderService: OrderServiceKT? = null

    @Autowired
    private val productService: ProductServiceKT? = null

    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    @Resource(name = "Orders")
    private val orders: Region<Long, Order>? = null

    @Resource(name = "Products")
    private val products: Region<Long, Product>? = null

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startGemFireServer(CascadingFunctionServerKT::class.java)
        }
    }

    @Test
    fun customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull
    }

    @Test
    fun orderServiceWasConfiguredCorrectly() {

        assertThat(this.orderService).isNotNull
    }

    @Test
    fun productServiceWasConfiguredCorrectly() {

        assertThat(this.productService).isNotNull
    }

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers?.name).isEqualTo("Customers")
        assertThat(this.customers?.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isEmpty()
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull
        assertThat(this.orders?.name).isEqualTo("Orders")
        assertThat(this.orders?.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
        assertThat(this.orders).isEmpty()
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull
        assertThat(this.products?.name).isEqualTo("Products")
        assertThat(this.products?.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat(this.products).isEmpty()
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {

        val jonDoe = Customer(15L, EmailAddress("example@example.org"), "Jon", "Doe")

        this.customerService?.save(jonDoe)
    }

    @Test
    fun orderRepositoryWasAutoConfiguredCorrectly() {
        val address = Address("Sesame ", "London", "Japan")
        val order = Order(1L, 1L, address, address)

        this.orderService?.save(order)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {
        val product = Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need")

        this.productService?.save(product)

        assertThat(this.productService?.findById(1L)).isEqualTo(product)
    }
}