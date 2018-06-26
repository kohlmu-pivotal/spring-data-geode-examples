package examples.springdata.geode.function.kt.client.services

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.function.kt.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.function.kt.client.repo.CustomerRepositoryKTKT
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKTKT,
                        private val customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun listAllCustomersForEmailAddress(vararg emailAddresses: String): List<Customer> =
        customerFunctionExecutionsKT.listAllCustomersForEmailAddress(*emailAddresses)
}