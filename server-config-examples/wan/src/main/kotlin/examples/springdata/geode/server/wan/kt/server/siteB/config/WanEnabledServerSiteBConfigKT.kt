package examples.springdata.geode.server.wan.kt.server.siteB.config

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.wan.kt.server.repo.CustomerRepositoryKT
import org.apache.geode.cache.*
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.DiskStoreFactoryBean
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import java.io.File
import java.util.*

@CacheServerApplication(port = 0, locators = "localhost[10334]")
@EnableLocator(port = 10334)
@EnableManager(start = true, port = 2099)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[20334]", enableNetworkPartitionDetection = false, conserveSockets = false)
@EnableLogging
class WanEnabledServerSiteBConfigKT {
    private val faker = Faker()

    @Bean(name = ["DiskStore"])
    internal fun diskStore(gemFireCache: GemFireCache): DiskStoreFactoryBean {
        val diskStoreFactoryBean = DiskStoreFactoryBean()
        File("/tmp/" + faker.name().firstName()).mkdirs()
        val diskDirs = arrayOf(DiskStoreFactoryBean.DiskDir("/tmp/" + faker.name().firstName()))
        diskStoreFactoryBean.setDiskDirs(Arrays.asList<DiskStoreFactoryBean.DiskDir>(*diskDirs))
        diskStoreFactoryBean.setCache(gemFireCache)
        return diskStoreFactoryBean
    }

    @Bean
    internal fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>): RegionAttributesFactoryBean<Long, Customer> {
        val regionAttributesFactoryBean = RegionAttributesFactoryBean<Long, Customer>()
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes)
        return regionAttributesFactoryBean
    }

    @Bean
    internal fun partitionAttributes(): PartitionAttributesFactoryBean<*, *> {
        val partitionAttributesFactoryBean = PartitionAttributesFactoryBean<Long, Customer>()
        partitionAttributesFactoryBean.setTotalNumBuckets(13)
        partitionAttributesFactoryBean.setRedundantCopies(0)
        return partitionAttributesFactoryBean
    }

    @Bean("Customers")
    internal fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>): PartitionedRegionFactoryBean<*, *> {
        val partitionedRegionFactoryBean = PartitionedRegionFactoryBean<Long, Customer>()
        partitionedRegionFactoryBean.cache = gemFireCache
        partitionedRegionFactoryBean.setRegionName("Customers")
        partitionedRegionFactoryBean.dataPolicy = DataPolicy.PARTITION
        partitionedRegionFactoryBean.setAttributes(regionAttributes)
        return partitionedRegionFactoryBean
    }

    @Bean
    internal fun createGatewayReceiver(gemFireCache: GemFireCache): GatewayReceiverFactoryBean {
        val gatewayReceiverFactoryBean = GatewayReceiverFactoryBean(gemFireCache as Cache)
        gatewayReceiverFactoryBean.setStartPort(25000)
        gatewayReceiverFactoryBean.setEndPort(25010)
        return gatewayReceiverFactoryBean
    }
}