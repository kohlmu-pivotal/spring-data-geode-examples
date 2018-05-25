package org.springframework.data.examples.geode.function.kt.server.functions

import org.springframework.data.examples.geode.model.Product
import org.springframework.data.gemfire.function.annotation.GemfireFunction
import org.springframework.data.gemfire.function.annotation.RegionData
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductFunctionsKT {
    @GemfireFunction(id = "sumPricesForAllProductsFnc", HA = true, optimizeForWrite = false, hasResult = true)
    fun sumPricesForAllProductsFnc(@RegionData productData: Map<Long, Product>) =
        productData.asSequence().map { it.value }.sumBy { it.price }
}

fun <T> Sequence<T>.sumBy(selector: (T) -> BigDecimal): BigDecimal {
    val sum = BigDecimal(0)
    for (element in this) {
        sum.add(selector(element))
    }
    return sum
}
