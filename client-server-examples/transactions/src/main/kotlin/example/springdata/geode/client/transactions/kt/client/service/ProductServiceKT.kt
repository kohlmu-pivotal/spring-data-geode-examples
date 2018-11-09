package example.springdata.geode.client.transactions.kt.client.service

import example.springdata.geode.client.transactions.kt.client.repo.ProductRepositoryKT
import examples.springdata.geode.domain.Product
import org.springframework.stereotype.Service

@Service
class ProductServiceKT(private val productRepositoryKT: ProductRepositoryKT) {

    fun save(product: Product) = productRepositoryKT.save(product)

    fun findAll(): List<Product> = productRepositoryKT.findAll()

    fun findById(productId: Long): Product = productRepositoryKT.findById(productId).get()
}
