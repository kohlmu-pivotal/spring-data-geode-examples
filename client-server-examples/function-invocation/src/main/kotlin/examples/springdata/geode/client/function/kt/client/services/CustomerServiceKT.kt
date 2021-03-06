package examples.springdata.geode.client.function.kt.client.services

import examples.springdata.geode.client.function.kt.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.client.function.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT,
                        private val customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun listAllCustomersForEmailAddress(vararg emailAddresses: String): List<Customer> =
            customerFunctionExecutionsKT.listAllCustomersForEmailAddress(*emailAddresses)[0]
}