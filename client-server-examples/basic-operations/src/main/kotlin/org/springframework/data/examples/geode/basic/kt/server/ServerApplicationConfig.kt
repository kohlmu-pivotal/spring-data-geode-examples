package org.springframework.data.examples.geode.basic.kt.server

import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.util.LoggingCacheListener
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator

@Configuration
@EnableLocator
@CacheServerApplication
class ServerApplicationConfigKT {
    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<String, Customer>() as CacheListener<String, Customer>

    @Bean("customerRegion")
    internal fun customerRegion(gemfireCache: GemFireCache) = ReplicatedRegionFactoryBean<String, Customer>().also {
        it.cache = gemfireCache
        it.setRegionName("Customer")
        it.dataPolicy = DataPolicy.REPLICATE
        it.setCacheListeners(arrayOf(loggingCacheListener()))
    }
}