package examples.springdata.geode.client.clusterregion.kt.client.repo

import examples.springdata.geode.domain.Order
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository

@ClientRegion("Orders")
interface OrderRepositoryKT : CrudRepository<Order, Long> {
    override fun findAll(): List<Order>
}
