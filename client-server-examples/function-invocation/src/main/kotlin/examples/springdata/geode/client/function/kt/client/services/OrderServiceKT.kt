package examples.springdata.geode.client.function.kt.client.services

import examples.springdata.geode.client.function.kt.client.functions.OrderFunctionExecutionsKT
import examples.springdata.geode.client.function.kt.client.repo.OrderRepositoryKT
import examples.springdata.geode.domain.Order
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