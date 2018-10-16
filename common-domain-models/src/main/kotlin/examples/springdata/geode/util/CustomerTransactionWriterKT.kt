package examples.springdata.geode.util

import org.apache.commons.logging.LogFactory
import org.apache.geode.cache.TransactionEvent
import org.apache.geode.cache.TransactionWriter
import org.apache.geode.cache.TransactionWriterException
import org.apache.geode.internal.cache.TXEntryState
import org.apache.geode.internal.cache.TXEvent
import org.springframework.stereotype.Component

@Component(value = "customerTransactionWriter")
class CustomerTransactionWriterKT : TransactionWriter {
    override fun close() {}

    private val log = LogFactory.getLog(CustomerTransactionWriterKT::class.java)

    override fun beforeCommit(transactionEvent: TransactionEvent) {
        transactionEvent is TXEvent
        transactionEvent.events.firstOrNull {
            if (it is TXEntryState.TxEntryEventImpl) {
                it.key == 6L
            } else {
                false
            }
        }?.run { throw TransactionWriterException("Customer for Key: 6 is being changed. Failing transaction") }
    }
}