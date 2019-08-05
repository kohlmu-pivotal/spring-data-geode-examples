package examples.springdata.geode.client.serialization.kt.client.services

import examples.springdata.geode.client.common.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.client.serialization.kt.client.repo.CustomerRepositoryKT
import org.apache.geode.cache.Region
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service("customerServiceKT")
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) : CustomerServiceKT {

    @Resource(name = "Customers")
    private lateinit var customerRegion: Region<Long, Customer>

    override fun save(customer: Customer) = customerRepositoryKT.save(customer)

    override fun findAll(): List<Customer> = customerRepositoryKT.findAll()

    override fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    override fun numberEntriesStoredLocally() = customerRegion.size

    override fun numberEntriesStoredOnServer() = customerRegion.keySetOnServer().size

    override fun deleteById(id: Long) = customerRepositoryKT.deleteById(id)
}
