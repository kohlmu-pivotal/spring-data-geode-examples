package examples.springdata.geode.functions.cascading.kt.client.repo

import examples.springdata.geode.domain.Customer
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository

interface CustomerRepositoryKT : CrudRepository<Customer,Long>