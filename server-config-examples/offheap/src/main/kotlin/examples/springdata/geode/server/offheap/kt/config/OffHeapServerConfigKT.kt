package examples.springdata.geode.server.offheap.kt.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.offheap.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.EnableOffHeap
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication(logLevel = "error")
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableOffHeap(memorySize = "8192m", regionNames = ["Customers"])
class OffHeapServerConfigKT {

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Customers")
                isStatisticsEnabled = true
            }

    @Bean("Products")
    fun createProductRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Product>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Products")
                isStatisticsEnabled = true
                isOffHeap = true
            }
}