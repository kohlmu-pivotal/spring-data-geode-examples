package examples.springdata.geode.client.oql.kt.services

import examples.springdata.geode.client.oql.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.client.ClientCache
import org.apache.geode.cache.query.SelectResults
import org.springframework.data.gemfire.GemfireTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT,
                        private val customerTemplate: GemfireTemplate,
                        private val gemFireCache: ClientCache) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    fun findWithTemplate(queryString: String, vararg parameters: Any?): List<Customer> =
        customerTemplate.find<Customer>(queryString, *parameters)?.asList() ?: emptyList()

    fun <Customer> findByEmailAddressUsingIndex(emailAddress: String) =
        customerRepositoryKT.findByEmailAddressUsingIndex<Customer>(emailAddress)

    fun <Customer> findByFirstNameUsingIndex(firstName: String) =
        customerRepositoryKT.findByFirstNameUsingIndex<Customer>(firstName)

    fun <Customer> findByFirstNameLocalClientRegion(queryString: String, vararg parameters: Any): List<Customer> {
        val query = gemFireCache.localQueryService.newQuery(queryString)
        val selectResults = query.execute(parameters) as SelectResults<Customer>
        return selectResults.asList()
    }
}