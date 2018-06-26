package examples.springdata.geode.function.kt.client.functions

import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion
import java.math.BigDecimal

@OnRegion(region = "Orders")
interface OrderFunctionExecutionsKT {
    @FunctionId("sumPricesForAllProductsForOrderFnc")
    fun sumPricesForAllProductsForOrder(orderId: Long): List<BigDecimal>
}