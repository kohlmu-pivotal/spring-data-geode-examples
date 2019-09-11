package examples.springdata.geode.server.wan.event.filters;

import org.apache.geode.cache.wan.GatewayEventFilter;
import org.apache.geode.cache.wan.GatewayQueueEvent;
import org.springframework.stereotype.Component;

@Component
public class EvenNumberedKeyWanEventFilter implements GatewayEventFilter {
    @Override
    public boolean beforeEnqueue(GatewayQueueEvent event) {
        return (Long) event.getKey() % 2 == 0;
    }

    @Override
    public boolean beforeTransmit(GatewayQueueEvent event) {
        return true;
    }

    @Override
    public void afterAcknowledgement(GatewayQueueEvent event) {

    }
}
