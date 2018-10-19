package examples.springdata.geode.functions.cascading.client.functions

import examples.springdata.geode.domain.Order
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion
import org.springframework.data.gemfire.function.annotation.OnServers

@OnRegion(region="Orders")
interface OrderFunctionExecutionsKT {

    @FunctionId("FindOrdersForCustomers")
    fun findOrdersForCustomers(customerIds: Set<Long>): List<Order>
}