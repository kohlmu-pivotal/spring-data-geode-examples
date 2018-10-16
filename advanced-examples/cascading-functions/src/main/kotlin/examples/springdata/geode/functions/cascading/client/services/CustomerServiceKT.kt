package examples.springdata.geode.functions.cascading.client.services

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.functions.cascading.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.functions.cascading.client.repo.CustomerRepositoryKT
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT)
//,
//                        private val customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) {
{
    fun save(customer: Customer) = customerRepositoryKT.save(customer)
//    fun listAllCustomers(): List<Long> = customerFunctionExecutionsKT.listAllCustomers()
}