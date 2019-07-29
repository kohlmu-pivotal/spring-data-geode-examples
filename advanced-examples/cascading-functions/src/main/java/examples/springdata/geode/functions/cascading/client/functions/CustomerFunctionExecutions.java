package examples.springdata.geode.functions.cascading.client.functions;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

import java.util.List;

@OnRegion(region = "Customers")
public interface CustomerFunctionExecutions {

    @FunctionId("ListAllCustomers")
    List<List<Long>> listAllCustomers();
}
