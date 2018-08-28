package examples.springdata.geode.kt.client.entityregion.repo

import examples.springdata.geode.domain.Product
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository

@ClientRegion("Products")
interface ProductRepositoryKT : CrudRepository<Product, Long> {
    override fun findAll(): List<Product>
}
