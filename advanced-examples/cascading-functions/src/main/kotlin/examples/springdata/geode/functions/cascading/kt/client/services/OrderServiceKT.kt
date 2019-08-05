package examples.springdata.geode.functions.cascading.kt.client.services

import examples.springdata.geode.domain.Order
import examples.springdata.geode.functions.cascading.kt.client.functions.OrderFunctionExecutionsKT
import examples.springdata.geode.functions.cascading.kt.client.repo.OrderRepositoryKT
import org.springframework.stereotype.Service

@Service
class OrderServiceKT(private val orderRepositoryKT: OrderRepositoryKT,
                     private val orderFunctionExecutionsKT: OrderFunctionExecutionsKT) {

    fun save(order: Order) = orderRepositoryKT.save(order)
    fun findOrdersForCustomers(customerIds: List<Long>): List<Order> =
            orderFunctionExecutionsKT.findOrdersForCustomers(customerIds)
}