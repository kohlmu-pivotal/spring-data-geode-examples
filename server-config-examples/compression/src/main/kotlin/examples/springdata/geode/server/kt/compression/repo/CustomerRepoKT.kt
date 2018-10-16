package examples.springdata.geode.server.kt.compression.repo

import examples.springdata.geode.domain.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepoKT : CrudRepository<Customer, Long>
