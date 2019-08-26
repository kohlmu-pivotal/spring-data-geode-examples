package examples.springdata.geode.functions.cascading.kt.client.services

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.functions.cascading.kt.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.functions.cascading.kt.client.repo.CustomerRepositoryKT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT,
                        private val customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)
    fun listAllCustomers(): List<Long> = customerFunctionExecutionsKT.listAllCustomers()[0];
}