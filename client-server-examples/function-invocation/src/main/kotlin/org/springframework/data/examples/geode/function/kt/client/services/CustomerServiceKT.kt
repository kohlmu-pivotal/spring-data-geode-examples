package org.springframework.data.examples.geode.function.kt.client.services

import org.springframework.data.examples.geode.function.kt.client.functions.CustomerFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT,
                        private val customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun listAllCustomersForEmailAddress(vararg emailAddresses: String): List<Customer> =
        customerFunctionExecutionsKT.listAllCustomersForEmailAddress(*emailAddresses)
}