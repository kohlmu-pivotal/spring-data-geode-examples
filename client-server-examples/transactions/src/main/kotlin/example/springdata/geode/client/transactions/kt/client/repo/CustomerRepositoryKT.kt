package example.springdata.geode.client.transactions.kt.client.repo

import examples.springdata.geode.client.common.kt.client.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.mapping.annotation.ClientRegion

@ClientRegion("Customers")
interface CustomerRepositoryKT : BaseCustomerRepositoryKT
