package examples.springdata.geode.expiration.entity.kt.repo

import examples.springdata.geode.expiration.entity.kt.domain.CustomerKT
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.data.repository.CrudRepository

@Region("Customers")
interface CustomerRepositoryKT : CrudRepository<CustomerKT, Long>
