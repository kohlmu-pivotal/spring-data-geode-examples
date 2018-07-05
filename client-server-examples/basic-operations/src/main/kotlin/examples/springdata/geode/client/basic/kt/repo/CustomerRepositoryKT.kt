package examples.springdata.geode.client.basic.kt.repo

import org.springframework.data.examples.geode.common.kt.client.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion("Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT