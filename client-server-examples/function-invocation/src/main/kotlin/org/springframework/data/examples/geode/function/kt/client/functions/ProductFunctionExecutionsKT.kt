package org.springframework.data.examples.geode.function.kt.client.functions

import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion
import java.math.BigDecimal

@OnRegion(region = "Products")
interface ProductFunctionExecutionsKT {
    @FunctionId("sumPricesForAllProductsFnc")
    fun sumPricesForAllProducts(): List<BigDecimal>
}