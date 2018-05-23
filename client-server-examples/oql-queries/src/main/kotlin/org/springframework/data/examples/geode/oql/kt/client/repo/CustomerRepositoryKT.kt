package org.springframework.data.examples.geode.oql.kt.client.repo

import org.springframework.data.examples.geode.common.kt.client.repo.BaseCustomerRepository
import org.springframework.data.gemfire.repository.Query
import org.springframework.data.gemfire.repository.query.annotation.Hint
import org.springframework.data.gemfire.repository.query.annotation.Limit
import org.springframework.data.gemfire.repository.query.annotation.Trace

interface CustomerRepositoryKT : BaseCustomerRepository {

    @Trace
    @Limit(100)
    @Hint("emailAddressIndex")
    @Query("select * from /Customers customer where customer.emailAddress.value = $1")
    fun <T> findByEmailAddressUsingIndex(emailAddress: String): List<T>

    @Trace
    @Limit(100)
    @Query("select * from /Customers customer where customer.firstName = $1")
    fun <T> findByFirstNameUsingIndex(firstName: String): List<T>
}