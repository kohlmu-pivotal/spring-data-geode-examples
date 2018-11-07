package example.springdata.geode.server.syncqueues.listener;

import examples.springdata.geode.domain.OrderProductSummaryKey;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class OrderAsyncQueueListener implements AsyncEventListener {

    private Duration summaryDuration = Duration.ofSeconds(10);

    @Override
    public boolean processEvents(List<AsyncEvent> list) {
        new TreeMap<OrderProductSummaryKey, BigDecimal>(Comparator.comparing())
//        list.stream().
        return true;
    }
}
