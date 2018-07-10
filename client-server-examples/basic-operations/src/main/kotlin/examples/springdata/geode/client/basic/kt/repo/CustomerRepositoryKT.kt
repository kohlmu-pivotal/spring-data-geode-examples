package examples.springdata.geode.client.basic.kt.repo

import examples.springdata.geode.common.client.kt.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion("Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT