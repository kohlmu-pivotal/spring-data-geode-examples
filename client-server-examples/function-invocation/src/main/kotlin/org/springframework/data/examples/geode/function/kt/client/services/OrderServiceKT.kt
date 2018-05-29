package org.springframework.data.examples.geode.function.kt.client.services

import org.springframework.data.examples.geode.function.kt.client.functions.OrderFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.repo.OrderRepositoryKT
import org.springframework.data.examples.geode.model.Order
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderServiceKT(private val orderRepositoryKT: OrderRepositoryKT,
                     private val orderFunctionExecutionsKT: OrderFunctionExecutionsKT) {

    fun save(order: Order) = orderRepositoryKT.save(order)

    fun sumPricesForAllProductsForOrder(orderId: Long): List<BigDecimal> =
        orderFunctionExecutionsKT.sumPricesForAllProductsForOrder(orderId)

    fun findById(orderId: Long) = orderRepositoryKT.findById(orderId)
}