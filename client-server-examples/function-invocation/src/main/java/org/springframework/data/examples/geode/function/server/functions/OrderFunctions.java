package org.springframework.data.examples.geode.function.server.functions;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.examples.geode.model.Order;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

@Component
public class OrderFunctions {

	@GemfireFunction(id = "sumPricesForAllProductsForOrderFnc", HA = true, optimizeForWrite = false, hasResult = true)
	public BigDecimal sumPricesForAllProductsForOrderFnc(Long orderId, @RegionData Map<Long, Order> orderData) {
		return orderData.get(orderId).getTotal();
	}
}
