package example.springdata.geode.server.syncqueues.listener;

import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.OrderProductSummary;
import examples.springdata.geode.domain.OrderProductSummaryKey;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrderAsyncQueueListener implements AsyncEventListener {

    private Region summaryRegion;

    public OrderAsyncQueueListener(Region summaryRegion) {
        this.summaryRegion = summaryRegion;
    }

    @Override
    public boolean processEvents(List<AsyncEvent> list) {
        final Calendar calendar = Calendar.getInstance();
        final int seconds = calendar.get(Calendar.SECOND);
        final int mod = Math.floorMod(seconds, 10);
        calendar.set(Calendar.SECOND, (seconds - mod));
        HashMap<OrderProductSummaryKey, OrderProductSummary> summaryMap = new HashMap<>();
        list.forEach(asyncEvent -> {
            final Order order = (Order) asyncEvent.getDeserializedValue();
            order.getLineItems().forEach(lineItem -> {
                final OrderProductSummaryKey key = new OrderProductSummaryKey(lineItem.getProductId(), calendar.getTimeInMillis());
                OrderProductSummary orderProductSummary = summaryMap.get(key);
                if (orderProductSummary == null) {
                    orderProductSummary = new OrderProductSummary(key, new BigDecimal("0.00"));
                }
                orderProductSummary.setSummaryAmount(orderProductSummary.getSummaryAmount().add(lineItem.getTotal()));
                summaryMap.put(key, orderProductSummary);
            });
        });

        summaryMap.forEach((orderProductSummaryKey, orderProductSummary) -> {
            final OrderProductSummary productSummary = (OrderProductSummary) summaryRegion.get(orderProductSummaryKey);
            if (productSummary != null) {
                final BigDecimal newSummaryAmount = productSummary.getSummaryAmount().add(orderProductSummary.getSummaryAmount());
                summaryRegion.put(orderProductSummaryKey, new OrderProductSummary(orderProductSummaryKey, newSummaryAmount));
            } else {
                summaryRegion.put(orderProductSummaryKey, orderProductSummary);
            }
        });
        return true;
    }
}
