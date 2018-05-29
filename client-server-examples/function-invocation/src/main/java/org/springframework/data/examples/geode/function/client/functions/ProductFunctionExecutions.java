package org.springframework.data.examples.geode.function.client.functions;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Products")
public interface ProductFunctionExecutions {

	@FunctionId("sumPricesForAllProductsFnc")
	List<BigDecimal> sumPricesForAllProducts();
}