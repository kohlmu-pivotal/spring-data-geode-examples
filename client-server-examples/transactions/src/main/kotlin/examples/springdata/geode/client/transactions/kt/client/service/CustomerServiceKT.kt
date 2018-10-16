package examples.springdata.geode.client.transactions.kt.client.service

import examples.springdata.geode.client.transactions.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import org.apache.geode.cache.Region
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.annotation.Resource

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    @Resource(name = "Customers")
    private lateinit var customerRegion: Region<Long, Customer>

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun findAll(): List<Customer> = customerRepositoryKT.findAll()

    fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    fun numberEntriesStoredLocally(): Int = customerRegion.size

    fun numberEntriesStoredOnServer(): Int = customerRegion.keySetOnServer().size

    fun deleteById(id: Long) {
        customerRepositoryKT.deleteById(id)
    }

    @Transactional
    fun createFiveCustomers(): List<Customer> =
            arrayOf(Customer(1L, EmailAddress("1@1.com"), "John", "Melloncamp"),
                    Customer(2L, EmailAddress("2@2.com"), "Franky", "Hamilton"),
                    Customer(3L, EmailAddress("3@3.com"), "Sebastian", "Horner"),
                    Customer(4L, EmailAddress("4@4.com"), "Chris", "Vettel"),
                    Customer(5L, EmailAddress("5@5.com"), "Kimi", "Rosberg"))
                    .map { customerRepositoryKT.save(it) }
                    .toList()

    @Transactional
    fun updateCustomersSuccess() {
        customerRepositoryKT.save(Customer(2L, EmailAddress("2@2.com"), "Humpty", "Hamilton"))
    }

    @Transactional
    fun updateCustomersWithDelay(millisDelay: Int, customer: Customer) {
        customerRepositoryKT.save(customer)
        try {
            Thread.sleep(millisDelay.toLong())
        } catch (e: InterruptedException) {
        }

    }

    @Transactional
    fun updateCustomersFailure() {
        customerRepositoryKT.save(Customer(2L, EmailAddress("2@2.com"), "Numpty", "Hamilton"))
        throw IllegalArgumentException("This should fail the transactions")
    }
}