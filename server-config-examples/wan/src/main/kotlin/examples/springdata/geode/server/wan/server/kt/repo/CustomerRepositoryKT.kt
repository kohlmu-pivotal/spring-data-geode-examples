package examples.springdata.geode.server.wan.server.kt.repo

import examples.springdata.geode.domain.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepositoryKT : CrudRepository<Customer, Long>
