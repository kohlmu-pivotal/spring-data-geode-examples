package org.springframework.data.examples.geode.function.kt.server.functions

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.function.annotation.GemfireFunction
import org.springframework.data.gemfire.function.annotation.RegionData
import org.springframework.stereotype.Component

@Component
class CustomerFunctionsKT {
    @GemfireFunction(id = "listConsumersForEmailAddressesFnc", HA = true, optimizeForWrite = true, batchSize = 3, hasResult = true)
    fun listAllCustomersForEmailAddress(vararg emailAddresses: String, @RegionData customerData: Map<Long, Customer>) =
        customerData.asSequence().map { it.value }.filter { emailAddresses.contains(it.emailAddress.value) }.toList()
}