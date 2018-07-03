package examples.springdata.geode.client.client.function.kt.client.repo

import examples.springdata.geode.domain.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepositoryKT : CrudRepository<Order, Long>