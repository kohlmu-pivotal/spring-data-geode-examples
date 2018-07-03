package examples.springdata.geode.client.client.cq.kt.producer.services

import examples.springdata.geode.client.cq.kt.producer.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)
}