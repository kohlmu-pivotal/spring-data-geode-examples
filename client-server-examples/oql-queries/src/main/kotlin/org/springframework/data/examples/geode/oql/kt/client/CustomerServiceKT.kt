package org.springframework.data.examples.geode.oql.kt.client

import org.apache.geode.cache.Region
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.oql.kt.repository.CustomerRepositoryKT
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service
class CustomerServiceKT(private val customerRepositoryKT: CustomerRepositoryKT) {

    @Resource
    @Qualifier(OQLClientApplicationConfigKT.CUSTOMER_REGION_BEAN_NAME)
    private lateinit var customerRegion: Region<Long, Customer>

    fun save(customer: Customer) = customerRepositoryKT.save(customer)

    fun findAll(): List<Customer> = customerRepositoryKT.findAll()

    fun findById(id: Long): Optional<Customer> = customerRepositoryKT.findById(id)

    fun deleteById(id: Long) = customerRepositoryKT.deleteById(id)
}