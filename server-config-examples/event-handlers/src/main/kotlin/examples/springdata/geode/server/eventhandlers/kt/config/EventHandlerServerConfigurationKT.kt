package examples.springdata.geode.server.eventhandlers.kt.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.eventhandlers.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.*
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication
@EnableGemfireRepositories(basePackageClasses = arrayOf(CustomerRepositoryKT::class))
class EventHandlerServerConfigurationKT {

    @Bean
    internal fun loggingCacheListener(): CacheListener<*, *> = LoggingCacheListener<Any, Any>()

    @Bean
    internal fun customerCacheWriter(): CacheWriter<Long, Customer> = CustomerCacheWriterKT()

    @Bean
    internal fun productCacheLoader(): CacheLoader<Long, Product> = ProductCacheLoaderKT()

    @Bean("Products")
    internal fun createProductRegion(gemFireCache: GemFireCache, loggingCacheListener: CacheListener<*, *>,
                                     productCacheLoader: CacheLoader<Long, Product>): ReplicatedRegionFactoryBean<Long, Product> {
        return ReplicatedRegionFactoryBean<Long, Product>().apply {
            cache = gemFireCache
            setRegionName("Products")
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingCacheListener as CacheListener<Long, Product>))
            setCacheLoader(productCacheLoader)
        }
    }

    @Bean("Customers")
    internal fun createCustomerRegion(gemFireCache: GemFireCache,
                                      customerCacheWriter: CacheWriter<Long, Customer>,
                                      loggingCacheListener: CacheListener<*, *>): PartitionedRegionFactoryBean<Long, Customer> {
        return PartitionedRegionFactoryBean<Long, Customer>().apply {
            cache = gemFireCache
            setRegionName("Customers")
            dataPolicy = DataPolicy.PARTITION
            setCacheListeners(arrayOf(loggingCacheListener as CacheListener<Long, Customer>))
            setCacheWriter(customerCacheWriter)
        }
    }
}