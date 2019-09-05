package examples.springdata.geode.server.region.kt.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.region.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.PartitionAttributes
import org.apache.geode.cache.RegionAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@PeerCacheApplication
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class RegionTypeConfigurationKT {

    @Bean
    @Primary
    internal fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>): RegionAttributesFactoryBean<*, *> {
        val regionAttributesFactoryBean = RegionAttributesFactoryBean<Any, Any>()
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes)
        return regionAttributesFactoryBean
    }

    @Bean
    internal fun partitionAttributes(gemFireCache: GemFireCache): PartitionAttributesFactoryBean<*, *> {
        val partitionAttributesFactoryBean = PartitionAttributesFactoryBean<Long, Order>()
        partitionAttributesFactoryBean.setTotalNumBuckets(13)
        partitionAttributesFactoryBean.setRedundantCopies(1)
        return partitionAttributesFactoryBean
    }

    @Bean("Orders")
    internal fun createOrderRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Order> {
        val replicatedRegionFactoryBean = ReplicatedRegionFactoryBean<Long, Order>()
        replicatedRegionFactoryBean.cache = gemFireCache
        replicatedRegionFactoryBean.setRegionName("Orders")
        replicatedRegionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        return replicatedRegionFactoryBean
    }

    @Bean("Products")
    internal fun createProductRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Product> {
        val replicatedRegionFactoryBean = ReplicatedRegionFactoryBean<Long, Product>()
        replicatedRegionFactoryBean.cache = gemFireCache
        replicatedRegionFactoryBean.setRegionName("Products")
        replicatedRegionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        return replicatedRegionFactoryBean
    }

    @Bean("Customers")
    internal fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>): PartitionedRegionFactoryBean<Long, Customer> {
        val partitionedRegionFactoryBean = PartitionedRegionFactoryBean<Long, Customer>()
        partitionedRegionFactoryBean.cache = gemFireCache
        partitionedRegionFactoryBean.setRegionName("Customers")
        partitionedRegionFactoryBean.dataPolicy = DataPolicy.PARTITION
        partitionedRegionFactoryBean.attributes = regionAttributes
        return partitionedRegionFactoryBean
    }
}