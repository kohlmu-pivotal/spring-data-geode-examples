package org.springframework.data.examples.geode.cq.kt.client.services

import org.springframework.data.examples.geode.cq.kt.client.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.stereotype.Service

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    fun save(customer: Customer) = customerRepositoryKT.save(customer)
}