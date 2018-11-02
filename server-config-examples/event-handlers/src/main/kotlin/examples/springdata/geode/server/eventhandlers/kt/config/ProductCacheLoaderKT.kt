package examples.springdata.geode.server.eventhandlers.kt.config

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Product
import org.apache.geode.cache.CacheLoader
import org.apache.geode.cache.CacheLoaderException
import org.apache.geode.cache.LoaderHelper

import java.math.BigDecimal

class ProductCacheLoaderKT : CacheLoader<Long, Product> {
    private val faker = Faker()

    @Throws(CacheLoaderException::class)
    override fun load(loaderHelper: LoaderHelper<Long, Product>) =
            Product(loaderHelper.key, randomStringName(), randomPrice())

    private fun randomPrice(): BigDecimal = BigDecimal(faker.commerce().price())

    private fun randomStringName(): String = faker.commerce().productName()
}
