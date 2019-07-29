package examples.springdata.geode.server.asyncqueues.kt.config

import examples.springdata.geode.domain.*
import examples.springdata.geode.server.asyncqueues.kt.listener.OrderAsyncQueueListenerKT
import examples.springdata.geode.server.asyncqueues.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.*
import org.apache.geode.cache.asyncqueue.AsyncEventListener
import org.apache.geode.cache.asyncqueue.AsyncEventQueue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.wan.AsyncEventQueueFactoryBean

@PeerCacheApplication(logLevel = "error")
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class AsyncQueueServerConfigKT {

    @Bean
    internal fun orderAsyncEventListener(@Qualifier("OrderProductSummary") orderProductSummary: Region<OrderProductSummaryKey, OrderProductSummary>) =
            OrderAsyncQueueListenerKT(orderProductSummary)

    @Bean
    internal fun orderAsyncEventQueue(gemFireCache: GemFireCache, orderAsyncEventListener: AsyncEventListener) =
            AsyncEventQueueFactoryBean(gemFireCache as Cache).apply {
                setBatchTimeInterval(1000)
                setBatchSize(5)
                setAsyncEventListener(orderAsyncEventListener)
            }

    @Bean
    internal fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>) =
            RegionAttributesFactoryBean<Any, Any>().apply {
                setPartitionAttributes(partitionAttributes)
            }

    @Bean
    internal fun partitionAttributes(gemFireCache: GemFireCache): PartitionAttributesFactoryBean<*, *> =
            PartitionAttributesFactoryBean<Long, Order>().apply {
                setTotalNumBuckets(13)
                setRedundantCopies(0)
            }

    @Bean(name = ["OrderProductSummary"])
    internal fun createOrderProductSummaryRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<*, *>) =
            PartitionedRegionFactoryBean<Long, Order>().apply {
                cache = gemFireCache
                setRegionName("OrderProductSummary")
                dataPolicy = DataPolicy.PARTITION
                setAttributes(regionAttributes as RegionAttributes<Long, Order>)
            }

    @Bean("Orders")
    internal fun createOrderRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<*, *>, orderAsyncEventQueue: AsyncEventQueue) =
            PartitionedRegionFactoryBean<Long, Order>().apply {
                cache = gemFireCache
                setRegionName("Orders")
                dataPolicy = DataPolicy.PARTITION
                setAttributes(regionAttributes as RegionAttributes<Long, Order>)
                setAsyncEventQueues(arrayOf(orderAsyncEventQueue))
            }

    @Bean("Products")
    internal fun createProductRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Product>().apply {
                cache = gemFireCache
                setRegionName("Products")
                dataPolicy = DataPolicy.REPLICATE
            }

    @Bean("Customers")
    internal fun createCustomerRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.REPLICATE
            }
}