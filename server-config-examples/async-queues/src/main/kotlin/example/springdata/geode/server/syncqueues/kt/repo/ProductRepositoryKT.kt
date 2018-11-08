package example.springdata.geode.server.syncqueues.kt.repo

import examples.springdata.geode.domain.Product
import org.springframework.data.repository.CrudRepository

interface ProductRepositoryKT : CrudRepository<Product, Long>
