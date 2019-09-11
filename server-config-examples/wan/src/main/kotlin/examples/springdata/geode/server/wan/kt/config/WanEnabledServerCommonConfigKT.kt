package examples.springdata.geode.server.wan.kt.config

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.wan.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.PartitionAttributes
import org.apache.geode.cache.RegionAttributes
import org.apache.geode.cache.wan.GatewaySender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.DiskStoreFactoryBean
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import java.io.File

@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class WanEnabledServerCommonConfigKT {
    @Bean
    fun faker() = Faker()

    @Bean(name = ["DiskStore"])
    fun diskStore(gemFireCache: GemFireCache, faker: Faker): DiskStoreFactoryBean {
        val completed = File("/tmp/" + faker.name().firstName()).mkdirs()

        return DiskStoreFactoryBean().apply {
            setDiskDirs(listOf(DiskStoreFactoryBean.DiskDir("/tmp/" + faker.name().firstName())))
            setCache(gemFireCache)
        }
    }

    @Bean
    fun regionAttributes(partitionAttributes: PartitionAttributes<Any, Any>) =
            RegionAttributesFactoryBean<Any,Any>().apply {
                setPartitionAttributes(partitionAttributes)
            }

    @Bean
    fun partitionAttributes(gemFireCache: GemFireCache) =
            PartitionAttributesFactoryBean<Long, Customer>().apply {
                setTotalNumBuckets(13)
                setRedundantCopies(0)
            }

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>,
                             gatewaySender: GatewaySender) =
            PartitionedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.PARTITION
                attributes = regionAttributes
                setGatewaySenders(arrayOf(gatewaySender))
            }
}