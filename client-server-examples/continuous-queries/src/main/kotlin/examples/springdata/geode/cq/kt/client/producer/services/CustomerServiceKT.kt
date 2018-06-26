package examples.springdata.geode.cq.kt.client.producer.services

import examples.springdata.geode.cq.kt.client.producer.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)
}