package examples.springdata.geode.client.common.kt.client.service

import examples.springdata.geode.domain.Customer
import java.util.*

interface CustomerServiceKT {
    fun numberEntriesStoredLocally(): Int

    fun numberEntriesStoredOnServer(): Int

    fun save(customer: Customer) : Customer

    fun findAll(): Iterable<Customer>

    fun findById(id: Long): Optional<Customer>

    fun deleteById(id: Long)
}