package examples.springdata.geode.client.clusterregion.kt.client.service

import examples.springdata.geode.domain.Product
import examples.springdata.geode.client.clusterregion.kt.client.repo.ProductRepositoryKT
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductServiceKT(private val productRepository: ProductRepositoryKT) {

    fun save(product: Product) = productRepository.save(product)

    fun findAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Optional<Product> = productRepository.findById(id)
}