package examples.springdata.geode.client.oql.kt

import examples.springdata.geode.client.common.kt.server.ServerKT
import examples.springdata.geode.client.oql.kt.config.OQLClientApplicationConfigKT
import examples.springdata.geode.client.oql.kt.services.CustomerServiceKT
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [OQLClientApplicationConfigKT::class])
class OQLClientTestKT : ForkingClientServerIntegrationTestsSupport() {
    @Autowired
    private val customerService: CustomerServiceKT? = null

    @Resource(name = "Customers")
    private val customers: Region<Long, Customer>? = null

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(ServerKT::class.java)
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
    fun customerRepositoryWasAutoConfiguredCorrectly() {

        println("Inserting 3 entries for keys: 1, 2, 3")
        val john = Customer(1L, EmailAddress("2@2.com"), "John", "Smith")
        val frank = Customer(2L, EmailAddress("3@3.com"), "Frank", "Lamport")
        val jude = Customer(3L, EmailAddress("5@5.com"), "Jude", "Simmons")
        customerService!!.save(john)
        customerService.save(frank)
        customerService.save(jude)
        assertThat(customers!!.keySetOnServer().size).isEqualTo(3)

        var customer = customerService.findById(2L).get()
        assertThat(customer).isEqualTo(frank)
        println("Find customer with key=2 using GemFireRepository: $customer")
        var customerList: List<*> = customerService.findWithTemplate("select * from /Customers where id=$1", 2L)
        assertThat(customerList.size).isEqualTo(1)
        assertThat(customerList.contains(frank)).isTrue()
        println("Find customer with key=2 using GemFireTemplate: $customerList")

        customer = Customer(1L, EmailAddress("3@3.com"), "Jude", "Smith")
        customerService.save(customer)
        assertThat(customers.keySetOnServer().size).isEqualTo(3)

        customerList = customerService.findByEmailAddressUsingIndex<Customer>("3@3.com")
        assertThat(customerList.size).isEqualTo(2)
        assertThat(customerList.contains(frank)).isTrue()
        assertThat(customerList.contains(customer)).isTrue()
        println("Find customers with emailAddress=3@3.com: $customerList")

        customerList = customerService.findByFirstNameUsingIndex<Customer>("Frank")
        assertThat(customerList[0]).isEqualTo(frank)
        println("Find customers with firstName=Frank: $customerList")
        customerList = customerService.findByFirstNameUsingIndex<Customer>("Jude")
        assertThat(customerList.size).isEqualTo(2)
        assertThat(customerList.contains(jude)).isTrue()
        assertThat(customerList.contains(customer)).isTrue()
        println("Find customers with firstName=Jude: $customerList")

        customerList = customerService.findByFirstNameLocalClientRegion<Customer>("select * from /Customers where firstName=$1", "Jude")
        assertThat(customerList).isEmpty()
        println("Find customers with firstName=Jude on local client region: $customerList")
    }
}