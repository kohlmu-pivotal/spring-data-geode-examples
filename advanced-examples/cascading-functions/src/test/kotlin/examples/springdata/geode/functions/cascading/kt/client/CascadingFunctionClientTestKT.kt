package examples.springdata.geode.functions.cascading.kt.client

import examples.springdata.geode.domain.*
import examples.springdata.geode.functions.cascading.kt.client.config.CascadingFunctionClientConfigKT
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
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport
import org.springframework.data.gemfire.util.RegionUtils
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CascadingFunctionClientConfigKT::class])
class CascadingFunctionClientKTTest : ForkingClientServerIntegrationTestsSupport() {
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
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {
        assertThat(this.orders).isNotNull
        assertThat(this.orders?.name).isEqualTo("Orders")
        assertThat(this.orders?.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {
        assertThat(this.products).isNotNull
        assertThat(this.products?.name).isEqualTo("Products")
        assertThat(this.products?.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
    }

    @Test
    fun testMethod() {
        IntStream.rangeClosed(1, 10000).parallel().forEach { customerId -> customerService!!.save(Customer(Integer.toUnsignedLong(customerId), EmailAddress("2@2.com"), "John$customerId", "Smith$customerId")) }

        assertThat(customers!!.keySetOnServer().size).isEqualTo(10000)

        productService!!.save(Product(1L, "Apple iPod", BigDecimal("99.99"), "An Apple portable music player"))
        productService.save(Product(2L, "Apple iPad", BigDecimal("499.99"), "An Apple tablet device"))

        val product = Product(3L, "Apple macBook", BigDecimal("899.99"), "An Apple notebook computer")
        product.addAttribute("warranty", "included")

        productService.save(product)

        assertThat(products!!.keySetOnServer().size).isEqualTo(3)

        val random = Random(System.nanoTime())
        val address = Address("it", "doesn't", "matter")

        IntStream.rangeClosed(1, 10).forEach { orderId ->
            IntStream.rangeClosed(1, 10).forEach { customerId ->
                val order = Order(Integer.toUnsignedLong(orderId), customerId.toLong(), address)
                IntStream.rangeClosed(1, random.nextInt(3) + 1).forEach { i ->
                    val quantity = random.nextInt(3) + 1
                    val productId = (random.nextInt(3) + 1).toLong()
                    order.add(LineItem(productService.findById(productId), quantity))
                }
                orderService!!.save(order)
            }
        }

        assertThat(orders!!.keySetOnServer().size).isEqualTo(10)

        val listAllCustomers = customerService!!.listAllCustomers()
        assertThat(listAllCustomers.size).isEqualTo(10000)
        println("Number of customers retrieved from servers: " + listAllCustomers.size)

        val findOrdersForCustomer = orderService!!.findOrdersForCustomers(listAllCustomers)
        assertThat(findOrdersForCustomer.size).isEqualTo(10)
        println(findOrdersForCustomer)
    }
}