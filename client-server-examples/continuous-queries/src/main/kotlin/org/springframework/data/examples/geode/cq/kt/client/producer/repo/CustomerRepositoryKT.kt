package org.springframework.data.examples.geode.cq.kt.client.producer.repo

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository


@ClientRegion(name = "Customers")
interface CustomerRepositoryKT : CrudRepository<Customer, Long>