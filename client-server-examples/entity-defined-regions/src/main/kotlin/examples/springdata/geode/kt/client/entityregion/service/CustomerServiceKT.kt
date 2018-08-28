package examples.springdata.geode.kt.client.entityregion.service

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.kt.client.entityregion.repo.CustomerRepositoryKT
import org.apache.geode.cache.Region
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    @Resource(name = "Customers")
    private lateinit var customerRegion: Region<Long, Customer>

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun findAll(): List<Customer> = customerRepositoryKT.findAll()

    fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    fun numberEntriesStoredLocally() = customerRegion.size

    fun numberEntriesStoredOnServer() = customerRegion.keySetOnServer().size

    fun deleteById(id: Long) = customerRepositoryKT.deleteById(id)
}