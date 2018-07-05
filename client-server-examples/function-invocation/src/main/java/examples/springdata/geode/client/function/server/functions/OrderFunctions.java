package examples.springdata.geode.client.function.server.functions;

import examples.springdata.geode.domain.Order;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class OrderFunctions {

	@GemfireFunction(id = "sumPricesForAllProductsForOrderFnc", HA = true, optimizeForWrite = false, hasResult = true)
	public BigDecimal sumPricesForAllProductsForOrderFnc(Long orderId, @RegionData Map<Long, Order> orderData) {
		return orderData.get(orderId).getTotal();
	}
}
