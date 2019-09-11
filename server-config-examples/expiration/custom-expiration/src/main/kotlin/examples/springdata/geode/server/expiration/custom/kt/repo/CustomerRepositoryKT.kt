package examples.springdata.geode.server.expiration.custom.kt.repo

import examples.springdata.geode.domain.Customer
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.data.repository.CrudRepository

@Region("Customers")
interface CustomerRepositoryKT : CrudRepository<Customer, Long>
