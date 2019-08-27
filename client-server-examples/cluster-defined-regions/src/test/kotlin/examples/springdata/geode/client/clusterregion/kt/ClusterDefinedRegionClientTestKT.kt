package examples.springdata.geode.client.clusterregion.kt

import examples.springdata.geode.client.clusterregion.kt.client.config.ClusterDefinedRegionClientConfigKT
import examples.springdata.geode.client.clusterregion.kt.client.service.CustomerServiceKT
import examples.springdata.geode.client.clusterregion.kt.client.service.OrderServiceKT
import examples.springdata.geode.client.clusterregion.kt.client.service.ProductServiceKT
import examples.springdata.geode.client.clusterregion.kt.server.ClusterDefinedRegionServerKT
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
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import java.math.BigDecimal
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [ClusterDefinedRegionClientConfigKT::class])
class ClusterDefinedRegionClientTestKT : ForkingClientServerIntegrationTestsSupport() {

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
        @Throws(IOException::class)
        @JvmStatic
        fun setup() {
            startGemFireServer(ClusterDefinedRegionServerKT::class.java)
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

        assertThat<Long, Customer>(this.customers).isNotNull
        assertThat(this.customers!!.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat<Long, Customer>(this.customers).isEmpty()
    }

    @Test
    fun ordersRegionWasConfiguredCorrectly() {

        assertThat<Long, Order>(this.orders).isNotNull
        assertThat(this.orders!!.name).isEqualTo("Orders")
        assertThat(this.orders.fullPath).isEqualTo(RegionUtils.toRegionPath("Orders"))
        assertThat<Long, Order>(this.orders).isEmpty()
    }

    @Test
    fun productsRegionWasConfiguredCorrectly() {

        assertThat<Long, Product>(this.products).isNotNull
        assertThat(this.products!!.name).isEqualTo("Products")
        assertThat(this.products.fullPath).isEqualTo(RegionUtils.toRegionPath("Products"))
        assertThat<Long, Product>(this.products).isEmpty()
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {
        println("Inserting 3 entries for keys: 1, 2, 3")
        val john = Customer(1L, EmailAddress("2@2.com"), "John", "Smith")
        val frank = Customer(2L, EmailAddress("3@3.com"), "Frank", "Lamport")
        val jude = Customer(3L, EmailAddress("5@5.com"), "Jude", "Simmons")
        customerService!!.save(john)
        customerService.save(frank)
        customerService.save(jude)

        val localEntries = customerService.numberEntriesStoredLocally()
        assertThat(localEntries).isEqualTo(0)
        println("Entries on Client: $localEntries")
        val serverEntries = customerService.numberEntriesStoredOnServer()
        assertThat(serverEntries).isEqualTo(3)
        println("Entries on Server: $serverEntries")
        var all = customerService.findAll()
        assertThat(all.size).isEqualTo(3)
        all.forEach { customer -> println("\t Entry: \n \t\t $customer") }

        println("Updating entry for key: 2")
        var customer = customerService.findById(2L).get()
        assertThat(customer).isEqualTo(frank)
        println("Entry Before: $customer")
        val sam = Customer(2L, EmailAddress("4@4.com"), "Sam", "Spacey")
        customerService.save(sam)
        customer = customerService.findById(2L).get()
        assertThat(customer).isEqualTo(sam)
        println("Entry After: $customer")

        println("Removing entry for key: 3")
        customerService.deleteById(3L)
        assertThat<Customer>(customerService.findById(3L)).isEmpty

        println("Entries:")
        all = customerService.findAll()
        assertThat(all.size).isEqualTo(2)
        all.forEach { c -> println("\t Entry: \n \t\t $c") }
    }

    @Test
    fun orderRepositoryWasAutoConfiguredCorrectly() {
        val address = Address("Sesame ", "London", "Japan")
        val order = Order(1L, 1L, address, address)

        this.orderService!!.save(order)

        assertThat(this.orderService.findAll().size).isEqualTo(1)

        assertThat(this.orderService.findById(1L)).isEqualTo(order)
    }

    @Test
    fun productRepositoryWasAutoConfiguredCorrectly() {
        val product = Product(1L, "Thneed", BigDecimal.valueOf(9.98), "A fine thing that all people need")

        this.productService!!.save(product)

        assertThat(this.productService.findAll().size).isEqualTo(1)

        assertThat(this.productService.findById(1L)).isEqualTo(product)
    }
}