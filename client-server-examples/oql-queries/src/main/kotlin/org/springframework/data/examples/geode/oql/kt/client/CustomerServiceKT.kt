package org.springframework.data.examples.geode.oql.kt.client

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.oql.kt.client.repo.CustomerRepositoryKT
import org.springframework.data.gemfire.GemfireTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT,
                        private val customerTemplate: GemfireTemplate) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    fun <T> findWithTemplate(queryString: String, vararg parameters: Any) =
        customerTemplate.find<T>(queryString, *parameters)?.asList() ?: emptyList()

    fun <T> findByEmailAddressUsingIndex(emailAddress: String) = customerRepositoryKT.findByEmailAddressUsingIndex<T>(emailAddress)

    fun <T> findByFirstNameUsingIndex(firstName: String) = customerRepositoryKT.findByFirstNameUsingIndex<T>(firstName)
}