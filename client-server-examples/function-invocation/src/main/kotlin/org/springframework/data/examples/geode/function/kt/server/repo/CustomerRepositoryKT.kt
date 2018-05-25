package org.springframework.data.examples.geode.function.kt.server.repo

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.data.repository.CrudRepository

@Region("Customers")
interface CustomerRepositoryKT : CrudRepository<Customer, Long>