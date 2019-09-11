package examples.springdata.geode.server.wan.event.kt.filters

import org.apache.geode.cache.wan.GatewayEventFilter
import org.apache.geode.cache.wan.GatewayQueueEvent
import org.springframework.stereotype.Component

@Component
class EvenNumberedKeyWanEventFilterKT : GatewayEventFilter {
    override fun beforeEnqueue(event: GatewayQueueEvent<*, *>): Boolean = event.key as Long % 2 == 0L

    override fun beforeTransmit(event: GatewayQueueEvent<*, *>): Boolean = true

    override fun afterAcknowledgement(event: GatewayQueueEvent<*, *>) {

    }
}
