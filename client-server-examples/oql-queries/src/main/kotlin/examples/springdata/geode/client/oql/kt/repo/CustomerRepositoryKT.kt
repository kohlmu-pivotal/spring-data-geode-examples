package examples.springdata.geode.client.oql.kt.repo

import examples.springdata.geode.client.common.kt.client.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.repository.Query
import org.springframework.data.gemfire.repository.query.annotation.Hint
import org.springframework.data.gemfire.repository.query.annotation.Limit
import org.springframework.data.gemfire.repository.query.annotation.Trace

interface CustomerRepositoryKT : BaseCustomerRepositoryKT {

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