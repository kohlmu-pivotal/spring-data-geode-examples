package examples.springdata.geode.kt.client.entityregion.repo

import examples.springdata.geode.common.client.kt.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion("Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT