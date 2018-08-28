package examples.springdata.geode.kt.client.entityregion.service

import examples.springdata.geode.domain.Product
import examples.springdata.geode.kt.client.entityregion.repo.ProductRepositoryKT
import org.apache.geode.cache.Region
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service
class ProductServiceKT(private val productRepository: ProductRepositoryKT) {

    @Resource(name = "Products")
    private lateinit var productRegion: Region<Long, Product>

    fun save(product: Product) = productRepository.save(product)

    fun findAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Optional<Product> = productRepository.findById(id)
}