package org.springframework.data.examples.geode.function.client.functions;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Orders")
public interface OrderFunctionExecutions {

	@FunctionId("sumPricesForAllProductsForOrderFnc")
	List<BigDecimal> sumPricesForAllProductsForOrder(Long orderId);
}