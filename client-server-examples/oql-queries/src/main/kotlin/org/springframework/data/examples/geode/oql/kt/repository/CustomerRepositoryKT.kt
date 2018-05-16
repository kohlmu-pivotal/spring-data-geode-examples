package org.springframework.data.examples.geode.oql.kt.repository

import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.model.EmailAddress
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepositoryKT : CrudRepository<Customer, Long> {
    /**
     * Returns all [Customer]s.
     *
     * @return
     */
    override fun findAll(): List<Customer>

    /**
     * Finds all [Customer]s with the given lastname.
     *
     * @param lastname
     * @return
     */
    fun findByLastName(lastName: String): List<Customer>

    /**
     * Finds the Customer with the given [EmailAddress].
     *
     * @param emailAddress
     * @return
     */
    fun findByEmailAddress(emailAddress: EmailAddress): Customer
}