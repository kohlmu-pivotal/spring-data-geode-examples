package example.springdata.geode.client.security.kt.client.repo

import examples.springdata.geode.client.common.kt.client.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion(name = "Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT