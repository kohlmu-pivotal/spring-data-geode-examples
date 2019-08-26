package examples.springdata.geode.functions.cascading.kt.client.functions

import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion

@OnRegion(region = "Customers")
interface CustomerFunctionExecutionsKT {

    @FunctionId("ListAllCustomers")
    fun listAllCustomers(): List<List<Long>>
}