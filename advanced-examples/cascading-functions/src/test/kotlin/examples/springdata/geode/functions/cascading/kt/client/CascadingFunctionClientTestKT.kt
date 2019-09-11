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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [CascadingFunctionClientConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CascadingFunctionClientKTTest : ForkingClientServerIntegrationTestsSupport() {
    @Autowired
    lateinit var customerService: CustomerServiceKT

    @Autowired
    lateinit var orderService: OrderServiceKT

    @Autowired
    lateinit var productService: ProductServiceKT

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    @Resource(name = "Orders")
    lateinit var orders: Region<Long, Order>

    @Resource(name = "Products")
    lateinit var products: Region<Long, Product>

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startGemFireServer(CascadingFunctionServerKT::class.java)
        }
    }
    
    @Test
    fun customersRegionWasConfiguredCorrectly() {
        assertThat(this.customers.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {
        assertThat(this.orders.name).isEqualTo("Orders")
        assertThat(this.orders.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {
        assertThat(this.products.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
    }

    @Test
    fun functionsExecuteCorrectly() {
        IntStream.rangeClosed(1, 10000).parallel().forEach { customerId -> customerService.save(Customer(Integer.toUnsignedLong(customerId), EmailAddress("2@2.com"), "John$customerId", "Smith$customerId")) }

        assertThat(customers.keySetOnServer().size).isEqualTo(10000)

        productService.save(Product(1L, "Apple iPod", BigDecimal("99.99"), "An Apple portable music player"))
        productService.save(Product(2L, "Apple iPad", BigDecimal("499.99"), "An Apple tablet device"))

        val product = Product(3L, "Apple macBook", BigDecimal("899.99"), "An Apple notebook computer")
        product.addAttribute("warranty", "included")

        productService.save(product)

        assertThat(products.keySetOnServer().size).isEqualTo(3)

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
                orderService.save(order)
            }
        }

        assertThat(orders.keySetOnServer().size).isEqualTo(10)

        val listAllCustomers = customerService.listAllCustomers()
        assertThat(listAllCustomers.size).isEqualTo(10000)
        println("Number of customers retrieved from servers: " + listAllCustomers.size)

        val findOrdersForCustomer = orderService.findOrdersForCustomers(listAllCustomers)
        assertThat(findOrdersForCustomer.size).isEqualTo(10)
        println(findOrdersForCustomer)
    }
}