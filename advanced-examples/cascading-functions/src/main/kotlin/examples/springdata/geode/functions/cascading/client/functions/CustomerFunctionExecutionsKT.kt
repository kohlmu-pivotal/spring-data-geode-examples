package examples.springdata.geode.functions.cascading.client.functions

import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion

@OnRegion(region = "Customers")
@DependsOn("Customers")
interface CustomerFunctionExecutionsKT {

    @FunctionId("ListAllCustomers")
    fun listAllCustomers(): List<Long>
}