package examples.springdata.geode.server.asyncqueues.kt.listener

import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.OrderProductSummary
import examples.springdata.geode.domain.OrderProductSummaryKey
import org.apache.geode.cache.Region
import org.apache.geode.cache.asyncqueue.AsyncEvent
import org.apache.geode.cache.asyncqueue.AsyncEventListener
import java.math.BigDecimal
import java.util.*

class OrderAsyncQueueListenerKT(private val summaryRegion: Region<OrderProductSummaryKey, OrderProductSummary>) : AsyncEventListener {

    override fun processEvents(list: List<AsyncEvent<*, *>>): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        val summaryMap = HashMap<OrderProductSummaryKey, OrderProductSummary>()
        list.forEach { asyncEvent ->
            val order = asyncEvent.deserializedValue as Order
            for (lineItem in order.getLineItems()) {
                val key = OrderProductSummaryKey(lineItem.productId, calendar.timeInMillis)
                summaryMap[key] = summaryMap[key]?.also {
                    it.summaryAmount = it.summaryAmount.add(lineItem.total)
                } ?: run {
                    OrderProductSummary(key, BigDecimal("0.00"))
                }
            }
        }

        summaryMap.forEach { orderProductSummaryKey, orderProductSummary ->
            summaryRegion[orderProductSummaryKey]?.run {
                val newSummaryAmount = summaryAmount.add(orderProductSummary.summaryAmount)
                summaryRegion[orderProductSummaryKey] = OrderProductSummary(orderProductSummaryKey, newSummaryAmount)
            } ?: run {
                summaryRegion[orderProductSummaryKey] = orderProductSummary
            }
        }
        return true
    }
}
