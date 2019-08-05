package examples.springdata.geode.client.entityregion.kt.client.service

import examples.springdata.geode.domain.Order
import examples.springdata.geode.client.entityregion.kt.client.repo.OrderRepositoryKT
import org.apache.geode.cache.Region
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service
class OrderServiceKT(private val orderRepositoryKT: OrderRepositoryKT) {

    @Resource(name = "Orders")
    private lateinit var orderRegion: Region<Long, Order>

    fun save(order: Order) = orderRepositoryKT.save(order)

    fun findAll(): List<Order> = orderRepositoryKT.findAll()

    fun findById(id: Long): Optional<Order> = orderRepositoryKT.findById(id)
}