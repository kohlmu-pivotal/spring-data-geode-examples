package examples.springdata.geode.util

import org.apache.commons.logging.LogFactory
import org.apache.geode.cache.CacheEvent
import org.apache.geode.cache.TransactionEvent
import org.apache.geode.cache.util.TransactionListenerAdapter
import org.apache.geode.internal.cache.TXEntryState

class CustomerTransactionListener : TransactionListenerAdapter() {
    private val log = LogFactory.getLog(CustomerTransactionListener::class.java)

    override fun afterFailedCommit(event: TransactionEvent) {}

    override fun afterCommit(event: TransactionEvent) {}

    override fun afterRollback(event: TransactionEvent) =
            log.info("In afterRollback for entry(s) [${event.events.map { getEventInfo(it) }.toList()}]")

    private fun getEventInfo(cacheEvent: CacheEvent<*, *>?): String {
        return if (cacheEvent is TXEntryState.TxEntryEventImpl) {
            cacheEvent.newValue.toString()
        } else {
            cacheEvent.toString()
        }
    }
}