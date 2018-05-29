package org.springframework.data.examples.geode.function.server.functions;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.examples.geode.model.Product;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

@Component
public class ProductFunctions {

	@GemfireFunction(id = "sumPricesForAllProductsFnc", HA = true, optimizeForWrite = false, hasResult = true)
	public BigDecimal sumPricesForAllProductsFnc(@RegionData Map<Long, Product> productData) {
		return productData.values().stream().map(Product::getPrice).reduce(BigDecimal::add).get();
	}
}
