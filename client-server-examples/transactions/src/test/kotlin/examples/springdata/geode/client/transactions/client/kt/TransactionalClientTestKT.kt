package examples.springdata.geode.client.transactions.client.kt

import examples.springdata.geode.client.transactions.kt.client.config.TransactionalClientConfigKT
import examples.springdata.geode.client.transactions.kt.client.service.CustomerServiceKT
import examples.springdata.geode.client.transactions.kt.server.TransactionalServerKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
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
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [TransactionalClientConfigKT::class])
class TransactionalClientTestKT : ForkingClientServerIntegrationTestsSupport() {
    @Autowired
    private val customerService: CustomerServiceKT? = null

    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(TransactionalServerKT::class.java)
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
    fun repositoryWasAutoConfiguredCorrectly() {

        println("Number of Entries stored before = " + customerService!!.numberEntriesStoredOnServer())
        customerService.createFiveCustomers()
        assertThat(customerService.numberEntriesStoredOnServer()).isEqualTo(5)
        println("Number of Entries stored after = " + customerService.numberEntriesStoredOnServer())
        println("Customer for ID before (transaction commit success) = " + customerService.findById(2L))
        customerService.updateCustomersSuccess()
        assertThat(customerService.numberEntriesStoredOnServer()).isEqualTo(5)
        var customer = customerService.findById(2L).get()
        assertThat(customer.firstName).isEqualTo("Humpty")
        println("Customer for ID after (transaction commit success) = $customer")
        try {
            customerService.updateCustomersFailure()
        } catch (exception: IllegalArgumentException) {
        }

        customer = customerService.findById(2L).get()
        assertThat(customer.firstName).isEqualTo("Humpty")
        println("Customer for ID after (transaction commit failure) = " + customerService.findById(2L))

        val numpty = Customer(2L, EmailAddress("2@2.com"), "Numpty", "Hamilton")
        val frumpy = Customer(2L, EmailAddress("2@2.com"), "Frumpy", "Hamilton")
        customerService.updateCustomersWithDelay(1000, numpty)
        customerService.updateCustomersWithDelay(10, frumpy)
        customer = customerService.findById(2L).get()
        assertThat(customer).isEqualTo(frumpy)
        println("Customer for ID after 2 updates with delay = $customer")
    }
}