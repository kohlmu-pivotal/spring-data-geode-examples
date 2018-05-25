package org.springframework.data.examples.geode.function.kt.client.repo

import org.springframework.data.examples.geode.model.Product
import org.springframework.data.repository.CrudRepository

interface ProductRepositoryKT : CrudRepository<Product, Long>