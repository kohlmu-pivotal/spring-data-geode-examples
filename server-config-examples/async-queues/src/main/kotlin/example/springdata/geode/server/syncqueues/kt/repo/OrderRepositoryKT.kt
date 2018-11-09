package example.springdata.geode.server.syncqueues.kt.repo

import examples.springdata.geode.domain.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepositoryKT : CrudRepository<Order, Long>
