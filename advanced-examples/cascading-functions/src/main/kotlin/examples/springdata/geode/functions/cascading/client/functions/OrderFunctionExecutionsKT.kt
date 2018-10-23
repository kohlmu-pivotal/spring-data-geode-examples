package examples.springdata.geode.functions.cascading.client.functions

import examples.springdata.geode.domain.Order
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnServers

@OnServers
interface OrderFunctionExecutionsKT {

    @FunctionId("FindOrdersForCustomers")
    fun findOrdersForCustomers(customerIds: List<Long>): List<Order>
}