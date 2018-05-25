package org.springframework.data.examples.geode.function.kt.client.services

import org.springframework.data.examples.geode.function.kt.client.functions.ProductFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.repo.ProductRepositoryKT
import org.springframework.data.examples.geode.model.Product
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceKT(private val productRepositoryKT: ProductRepositoryKT,
                       private val productFunctionExecutionsKT: ProductFunctionExecutionsKT) {

    fun save(product: Product) = productRepositoryKT.save(product)

    fun sumPricesForAllProducts(): List<BigDecimal> = productFunctionExecutionsKT.sumPricesForAllProducts()

    fun findById(productId: Long): Product = productRepositoryKT.findById(productId).get()
}