package examples.springdata.geode.client.function.client.functions;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

import java.math.BigDecimal;
import java.util.List;

@OnRegion(region = "Orders")
public interface OrderFunctionExecutions {

	@FunctionId("sumPricesForAllProductsForOrderFnc")
	List<BigDecimal> sumPricesForAllProductsForOrder(Long orderId);
}