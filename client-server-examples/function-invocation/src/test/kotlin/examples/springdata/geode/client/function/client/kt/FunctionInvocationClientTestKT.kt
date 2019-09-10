package examples.springdata.geode.client.function.client.kt

import examples.springdata.geode.client.function.kt.client.config.FunctionInvocationClientApplicationConfigKT
import examples.springdata.geode.client.function.kt.client.services.CustomerServiceKT
import examples.springdata.geode.client.function.kt.client.services.OrderServiceKT
import examples.springdata.geode.client.function.kt.client.services.ProductServiceKT
import examples.springdata.geode.client.function.kt.server.FunctionServerKT
import examples.springdata.geode.domain.*
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
import java.io.IOException
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import java.util.stream.LongStream
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [FunctionInvocationClientApplicationConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FunctionInvocationClientTestKT : ForkingClientServerIntegrationTestsSupport() {
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
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(FunctionServerKT::class.java)
        }
    }

    @Test
    fun customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull
    }

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers!!.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isEmpty()
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {

        assertThat(this.orders).isNotNull
        assertThat(this.orders!!.name).isEqualTo("Orders")
        assertThat(this.orders.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
        assertThat(this.orders).isEmpty()
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat(this.products).isNotNull
        assertThat(this.products!!.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat(this.products).isEmpty()
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
    fun functionsExecuteCorrectly() {
        createCustomerData()

        val cust = customerService!!.listAllCustomersForEmailAddress("2@2.com", "3@3.com")
        assertThat(cust.size).isEqualTo(2)
        println("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t $cust")

        createProducts()
        var sum = productService!!.sumPricesForAllProducts()[0]
        assertThat(sum).isEqualTo(BigDecimal.valueOf(1499.97))
        println("Running function to sum up all product prices: \n\t$sum")

        createOrders()

        sum = orderService!!.sumPricesForAllProductsForOrder(1L)[0]
        assertThat(sum).isGreaterThanOrEqualTo(BigDecimal.valueOf(99.99))
        println("Running function to sum up all order lineItems prices for order 1: \n\t$sum")
        val order = orderService.findById(1L)
        println("For order: \n\t $order")
    }

    fun createCustomerData() {

        println("Inserting 3 entries for keys: 1, 2, 3")
        customerService!!.save(Customer(1L, EmailAddress("2@2.com"), "John", "Smith"))
        customerService.save(Customer(2L, EmailAddress("3@3.com"), "Frank", "Lamport"))
        customerService.save(Customer(3L, EmailAddress("5@5.com"), "Jude", "Simmons"))
        assertThat(customers!!.keySetOnServer().size).isEqualTo(3)
    }

    fun createProducts() {
        productService!!.save(Product(1L, "Apple iPod", BigDecimal("99.99"),
                "An Apple portable music player"))
        productService.save(Product(2L, "Apple iPad", BigDecimal("499.99"),
                "An Apple tablet device"))
        val macbook = Product(3L, "Apple macBook", BigDecimal("899.99"),
                "An Apple notebook computer")
        macbook.addAttribute("warranty", "included")
        productService.save(macbook)
        assertThat(products!!.keySetOnServer().size).isEqualTo(3)
    }

    fun createOrders() {
        val random = Random()
        val address = Address("it", "doesn't", "matter")
        LongStream.rangeClosed(1, 100).forEach { orderId ->
            LongStream.rangeClosed(1, 3).forEach { customerId ->
                val order = Order(orderId, customerId, address)
                IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach { lineItemCount ->
                    val quantity = random.nextInt(3) + 1
                    val productId = (random.nextInt(3) + 1).toLong()
                    order.add(LineItem(productService!!.findById(productId), quantity))
                }
                orderService!!.save(order)
            }
        }
        assertThat(orders!!.keySetOnServer().size).isEqualTo(100)
    }
}