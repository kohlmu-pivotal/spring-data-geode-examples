package example.springdata.geode.client.security.kt.client.repo

import examples.springdata.geode.common.client.kt.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion(name = "Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT