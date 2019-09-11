package examples.springdata.geode.client.serrialization.client.kt

import examples.springdata.geode.client.serialization.kt.client.config.PdxSerializationClientConfigKT
import examples.springdata.geode.client.serialization.kt.client.services.CustomerServiceKT
import examples.springdata.geode.client.serialization.kt.server.SerializationServerKT
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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [PdxSerializationClientConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SerializationClientTestKT : ForkingClientServerIntegrationTestsSupport() {
    @Autowired
    lateinit var customerService: CustomerServiceKT

    @Resource(name = "Customers")
    lateinit var customers: Region<Long, Customer>

    companion object {
        @BeforeClass
        @JvmStatic
        @Throws(IOException::class)
        fun setup() {
            startGemFireServer(SerializationServerKT::class.java)
        }
    }

    @Test
    fun customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull
    }

    @Test
    fun customersRegionWasConfiguredCorrectly() {

        assertThat(this.customers).isNotNull
        assertThat(this.customers.name).isEqualTo("Customers")
        assertThat(this.customers.fullPath).isEqualTo(RegionUtils.toRegionPath("Customers"))
        assertThat(this.customers).isEmpty()
    }

    @Test
    fun repositoryWasAutoConfiguredCorrectly() {

        println("Inserting 3 entries for keys: 1, 2, 3")
        val john = Customer(1L, EmailAddress("2@2.com"), "John", "Smith")
        val frank = Customer(2L, EmailAddress("3@3.com"), "Frank", "Lamport")
        val jude = Customer(3L, EmailAddress("5@5.com"), "Jude", "Simmons")
        customerService.save(john)
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
        assertThat(customerService.findById(3L)).isEmpty

        println("Entries:")
        all = customerService.findAll()
        assertThat(all.size).isEqualTo(2)
        all.forEach { c -> println("\t Entry: \n \t\t $c") }
    }
}