package examples.springdata.geode.client.function.server.functions;

import examples.springdata.geode.domain.Product;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ProductFunctions {

    @GemfireFunction(id = "sumPricesForAllProductsFnc", HA = true, optimizeForWrite = false, hasResult = true)
    public BigDecimal sumPricesForAllProductsFnc(@RegionData Map<Long, Product> productData) {
        return productData.values().stream().map(Product::getPrice).reduce(BigDecimal::add).get();
    }
}
