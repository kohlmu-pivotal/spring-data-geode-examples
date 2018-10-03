package examples.springdata.geode.client.common.kt.client.service

import examples.springdata.geode.domain.Customer
import java.util.*

interface CustomerServiceKT<T>{
    fun numberEntriesStoredLocally(): Int

    fun numberEntriesStoredOnServer(): Int

    fun save(customer: Customer) : Customer

    fun findAll(): Iterable<T>

    fun findById(id: Long): Optional<T>

    fun deleteById(id: Long)
}