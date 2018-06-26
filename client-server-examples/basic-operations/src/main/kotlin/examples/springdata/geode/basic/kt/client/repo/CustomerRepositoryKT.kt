package examples.springdata.geode.basic.kt.client.repo

import org.springframework.data.examples.geode.common.kt.client.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion("Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT