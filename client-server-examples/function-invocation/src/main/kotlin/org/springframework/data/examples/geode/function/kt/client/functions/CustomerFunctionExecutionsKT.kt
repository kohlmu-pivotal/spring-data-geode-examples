package org.springframework.data.examples.geode.function.kt.client.functions

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion

@OnRegion(region = "Customers")
interface CustomerFunctionExecutionsKT {
    @FunctionId("listConsumersForEmailAddressesFnc")
    fun listAllCustomersForEmailAddress(vararg emailAddresses: String): List<Customer>
}