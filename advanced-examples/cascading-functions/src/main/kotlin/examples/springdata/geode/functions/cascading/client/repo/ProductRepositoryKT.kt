package examples.springdata.geode.functions.cascading.client.repo

import examples.springdata.geode.domain.Product
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.mapping.annotation.ClientRegion
import org.springframework.data.repository.CrudRepository

@ClientRegion("Products")
@DependsOn("Products")
interface ProductRepositoryKT : CrudRepository<Product, Long>