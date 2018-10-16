package examples.springdata.geode.functions.cascading.client.functions

import examples.springdata.geode.domain.Order
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnServers

@OnServers
@DependsOn("Orders")
interface OrderFunctionExecutionsKT {

    @FunctionId("FindOrdersForCustomers")
    fun findOrdersForCustomers(customerIds: Set<Long>): List<Order>
}