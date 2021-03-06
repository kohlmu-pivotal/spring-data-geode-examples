package examples.springdata.geode.client.clusterregion.kt.server.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.*

@EnableLocator(host = "localhost", port = 10334)
@EnableManager(start = true)
@EnableHttpService
@CacheServerApplication(port = 0, logLevel = "error", useClusterConfiguration = true)
@EnableClusterConfiguration(useHttp = true)
class ClusterDefinedRegionServerConfigKT {

    @Bean("loggingCacheListener")
    internal fun loggingCacheListener() = LoggingCacheListener<Long, Customer>()

    @Bean("Customers")
    protected fun customerRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Customer>().apply {
            cache = gemfireCache
            setRegionName("Customers")
            scope = Scope.DISTRIBUTED_ACK
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, Customer>))
        }

    @Bean("Orders")
    protected fun orderRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Order>().apply {
            cache = gemfireCache
            setRegionName("Orders")
            scope = Scope.DISTRIBUTED_ACK
            dataPolicy = DataPolicy.REPLICATE
        }

    @Bean("Products")
    protected fun productRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Product>().apply {
            cache = gemfireCache
            setRegionName("Products")
            scope = Scope.DISTRIBUTED_ACK
            dataPolicy = DataPolicy.REPLICATE
        }

}