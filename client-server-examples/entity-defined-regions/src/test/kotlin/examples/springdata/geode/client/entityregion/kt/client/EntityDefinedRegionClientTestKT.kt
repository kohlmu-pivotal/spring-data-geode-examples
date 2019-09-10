package examples.springdata.geode.client.entityregion.kt.client

import examples.springdata.geode.client.kt.entityregion.client.config.EntityDefinedRegionClientConfigKT
import examples.springdata.geode.client.kt.entityregion.client.service.CustomerServiceKT
import examples.springdata.geode.client.kt.entityregion.server.EntityDefinedRegionServerKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [EntityDefinedRegionClientConfigKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EntityDefinedRegionClientTestKT : ForkingClientServerIntegrationTestsSupport() {
    @Autowired
    lateinit var customerService: CustomerServiceKT

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startGemFireServer(EntityDefinedRegionServerKT::class.java)
        }
    }

    @Test
    fun customerServiceWasConfiguredCorrectly() {

        assertThat(this.customerService).isNotNull
    }

    @Test
    fun customerRepositoryWasAutoConfiguredCorrectly() {

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
        assertThat<Customer>(customerService.findById(3L)).isEmpty

        println("Entries:")
        all = customerService.findAll()
        assertThat(all.size).isEqualTo(2)
        all.forEach { c -> println("\t Entry: \n \t\t $c") }
    }
}