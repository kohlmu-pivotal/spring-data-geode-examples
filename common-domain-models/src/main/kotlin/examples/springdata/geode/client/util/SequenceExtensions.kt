package examples.springdata.geode.client.util

import java.math.BigDecimal

fun <T> Sequence<T>.sumBy(selector: (T) -> BigDecimal): BigDecimal {
    var sum = BigDecimal.ZERO
    for (element in this) {
        sum = sum.add(selector(element))
    }
    return sum
}
