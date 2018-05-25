package org.springframework.data.examples.geode.function.kt.client.repo

import org.springframework.data.examples.geode.model.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepositoryKT : CrudRepository<Order, Long>