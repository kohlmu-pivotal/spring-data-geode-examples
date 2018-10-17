package examples.springdata.geode.functions.cascading.client.repo

import examples.springdata.geode.domain.Customer
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.data.repository.CrudRepository

@Region("Customers")
@DependsOn("Customers")
interface CustomerRepositoryKT : CrudRepository<Customer,Long>