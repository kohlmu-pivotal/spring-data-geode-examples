package examples.springdata.geode.client.cq.kt.producer.repo

import examples.springdata.geode.domain.Customer
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository


@ClientRegion(name = "Customers")
interface CustomerRepositoryKT : CrudRepository<Customer, Long>