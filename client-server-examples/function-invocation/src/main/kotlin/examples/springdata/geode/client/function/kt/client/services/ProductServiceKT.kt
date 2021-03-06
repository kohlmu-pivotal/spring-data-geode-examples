package examples.springdata.geode.client.function.kt.client.services

import examples.springdata.geode.client.function.kt.client.functions.ProductFunctionExecutionsKT
import examples.springdata.geode.client.function.kt.client.repo.ProductRepositoryKT
import examples.springdata.geode.domain.Product
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceKT(private val productRepositoryKT: ProductRepositoryKT,
                       private val productFunctionExecutionsKT: ProductFunctionExecutionsKT) {

    fun save(product: Product) = productRepositoryKT.save(product)

    fun sumPricesForAllProducts(): List<BigDecimal> = productFunctionExecutionsKT.sumPricesForAllProducts()

    fun findById(productId: Long): Product = productRepositoryKT.findById(productId).get()
}