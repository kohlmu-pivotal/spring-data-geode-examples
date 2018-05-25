package org.springframework.data.examples.geode.function.kt.server

import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.examples.geode.common.kt.server.ServerApplicationConfigKT
import org.springframework.data.examples.geode.function.kt.server.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.model.Order
import org.springframework.data.examples.geode.model.Product
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@EnableLocator
@CacheServerApplication(port = 0)
@EnableGemfireFunctions
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class FunctionServerApplicationConfigKT(applicationContext: ApplicationContext) : ServerApplicationConfigKT(applicationContext) {

    private fun loggingOrderCacheListener(): CacheListener<Long, Order> =
        applicationContext.getBean("loggingCacheListener") as CacheListener<Long, Order>

    private fun loggingProductCacheListener(): CacheListener<Long, Product> =
        applicationContext.getBean("loggingCacheListener") as CacheListener<Long, Product>

    @Bean("Products")
    protected fun productRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Product>().apply {
            cache = gemfireCache
            setRegionName("Products")
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingProductCacheListener()))
        }

    @Bean("Orders")
    protected fun orderRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Order>().apply {
            cache = gemfireCache
            setRegionName("Orders")
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingOrderCacheListener()))
        }
}