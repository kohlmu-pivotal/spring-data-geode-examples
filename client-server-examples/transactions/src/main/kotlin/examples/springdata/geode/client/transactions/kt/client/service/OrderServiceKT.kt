package examples.springdata.geode.client.transactions.kt.client.service

import examples.springdata.geode.client.transactions.kt.client.repo.OrderRepositoryKT
import examples.springdata.geode.domain.Order
import org.springframework.stereotype.Service

@Service
class OrderServiceKT(private val orderRepositoryKT: OrderRepositoryKT) {

    fun save(order: Order) = orderRepositoryKT.save(order)

    fun findAll(): List<Order> = orderRepositoryKT.findAll()

    fun findById(orderId: Long): Order = orderRepositoryKT.findById(orderId).get()
}
