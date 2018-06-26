package examples.springdata.geode.function.kt.server.functions

import examples.springdata.geode.domain.Order
import org.springframework.data.gemfire.function.annotation.GemfireFunction
import org.springframework.data.gemfire.function.annotation.RegionData
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderFunctionsKT {
    @GemfireFunction(id = "sumPricesForAllProductsForOrderFnc", HA = true, optimizeForWrite = false, hasResult = true)
    fun sumPricesForAllProductsForOrderFnc(orderId: Long, @RegionData orderData: Map<Long, Order>) =
        orderData[orderId]?.total ?: BigDecimal.ZERO
}