package examples.springdata.geode.function.kt.client.repo

import examples.springdata.geode.common.client.kt.repo.BaseCustomerRepositoryKT
import org.springframework.data.gemfire.repository.Query
import org.springframework.data.gemfire.repository.query.annotation.Hint
import org.springframework.data.gemfire.repository.query.annotation.Limit
import org.springframework.data.gemfire.repository.query.annotation.Trace

interface CustomerRepositoryKTKT : BaseCustomerRepositoryKT {

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