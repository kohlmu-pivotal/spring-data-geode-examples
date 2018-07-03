package examples.springdata.geode.client.client.function.client.functions;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

import java.math.BigDecimal;
import java.util.List;

@OnRegion(region = "Products")
public interface ProductFunctionExecutions {

	@FunctionId("sumPricesForAllProductsFnc")
	List<BigDecimal> sumPricesForAllProducts();
}