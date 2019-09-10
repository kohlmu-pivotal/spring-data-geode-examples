package examples.springdata.geode.server.wan.kt.server.siteA.config

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.wan.kt.server.repo.CustomerRepositoryKT
import org.apache.geode.cache.*
import org.apache.geode.cache.wan.GatewaySender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.DiskStoreFactoryBean
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.EnableManager
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean
import java.io.File
import java.util.*

@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@CacheServerApplication(port = 0, locators = "localhost[20334]", logLevel = "error")
@EnableLocator(port = 20334)
@EnableManager(start = true, port = 1099)
@EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[10334]", enableNetworkPartitionDetection = false, conserveSockets = false)
class WanEnabledServerSiteAConfigKT {
    @Bean
    fun getFaker() = Faker()

    @Bean(name = ["DiskStore"])
    fun diskStore(gemFireCache: GemFireCache,faker: Faker): DiskStoreFactoryBean {
        val diskStoreFactoryBean = DiskStoreFactoryBean()
        File("/tmp/" + faker.name().firstName()).mkdirs()
        val diskDirs = arrayOf(DiskStoreFactoryBean.DiskDir("/tmp/" + faker.name().firstName()))
        diskStoreFactoryBean.setDiskDirs(Arrays.asList<DiskStoreFactoryBean.DiskDir>(*diskDirs))
        diskStoreFactoryBean.setCache(gemFireCache)
        return diskStoreFactoryBean
    }

    @Bean
    fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>): RegionAttributesFactoryBean<*, *> {
        val regionAttributesFactoryBean = RegionAttributesFactoryBean<Long, Customer>()
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes)
        return regionAttributesFactoryBean
    }

    @Bean
    fun partitionAttributes(gemFireCache: GemFireCache): PartitionAttributesFactoryBean<*, *> {
        val partitionAttributesFactoryBean = PartitionAttributesFactoryBean<Long, Customer>()
        partitionAttributesFactoryBean.setTotalNumBuckets(13)
        partitionAttributesFactoryBean.setRedundantCopies(0)
        return partitionAttributesFactoryBean
    }

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>, gatewaySender: GatewaySender): PartitionedRegionFactoryBean<*, *> {
        val partitionedRegionFactoryBean = PartitionedRegionFactoryBean<Long, Customer>()
        partitionedRegionFactoryBean.cache = gemFireCache
        partitionedRegionFactoryBean.setRegionName("Customers")
        partitionedRegionFactoryBean.dataPolicy = DataPolicy.PARTITION
        partitionedRegionFactoryBean.setAttributes(regionAttributes)
        partitionedRegionFactoryBean.setGatewaySenders(arrayOf(gatewaySender))
        return partitionedRegionFactoryBean
    }

    @Bean
    @DependsOn("DiskStore")
    fun createGatewaySender(gemFireCache: GemFireCache): GatewaySenderFactoryBean {
        val gatewaySenderFactoryBean = GatewaySenderFactoryBean(gemFireCache as Cache)
        gatewaySenderFactoryBean.setBatchSize(15)
        gatewaySenderFactoryBean.setBatchTimeInterval(1000)
        gatewaySenderFactoryBean.setRemoteDistributedSystemId(2)
        gatewaySenderFactoryBean.isPersistent = false
        gatewaySenderFactoryBean.setDiskStoreRef("DiskStore")
        return gatewaySenderFactoryBean
    }
}