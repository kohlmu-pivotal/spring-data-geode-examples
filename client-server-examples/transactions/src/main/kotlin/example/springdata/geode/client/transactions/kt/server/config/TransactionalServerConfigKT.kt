package example.springdata.geode.client.transactions.kt.server.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableLocator
@EnableTransactionManagement
@EnableGemfireCacheTransactions
@EnableClusterConfiguration(useHttp = true)
@EnableManager(start = true)
@EnableHttpService
@CacheServerApplication
class TransactionalServerConfigKT {

    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<Long, Customer>()

    @Bean
    internal fun createCustomerRegion(gemfireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Customer> =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.REPLICATE
                scope = Scope.DISTRIBUTED_ACK
                setCacheListeners(arrayOf(loggingCacheListener()))
            }

    @Bean
    internal fun createOrderRegion(gemfireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Order> =
            ReplicatedRegionFactoryBean<Long, Order>().apply {
                cache = gemfireCache
                setRegionName("Orders")
                dataPolicy = DataPolicy.REPLICATE
                setScope(Scope.DISTRIBUTED_ACK)
            }

    @Bean
    internal fun createProductRegion(gemfireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Product> =
            ReplicatedRegionFactoryBean<Long, Product>().apply {
                cache = gemfireCache
                setRegionName("Products")
                dataPolicy = DataPolicy.REPLICATE
                setScope(Scope.DISTRIBUTED_ACK)
            }
}
