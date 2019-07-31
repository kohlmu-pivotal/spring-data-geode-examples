package example.springdata.geode.server.wan.kt.config

import com.github.javafaker.Faker
import example.springdata.geode.server.wan.repo.CustomerRepository
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.*
import org.apache.geode.cache.wan.GatewaySender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.DiskStoreFactoryBean
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean
import java.io.File

@Configuration
@EnableGemfireRepositories(basePackageClasses = arrayOf(CustomerRepository::class))
class WanEnableServerConfigKT {
    private val faker = Faker()

    @Bean(name = ["DiskStore"])
    internal fun diskStore(gemFireCache: GemFireCache): DiskStoreFactoryBean {
        File("/tmp/" + faker.name().firstName()).mkdirs()

        return DiskStoreFactoryBean().apply {
            setDiskDirs(listOf(DiskStoreFactoryBean.DiskDir("/tmp/" + faker.name().firstName())))
            setCache(gemFireCache)
        }
    }

    @Bean
    internal fun customerCacheListener() = LoggingCacheListener<Long, Customer>()

    @Bean
    internal fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>) = RegionAttributesFactoryBean().apply {
        setPartitionAttributes(partitionAttributes)
    }

    @Bean
    internal fun partitionAttributes(gemFireCache: GemFireCache) = PartitionAttributesFactoryBean<Long, Customer>().apply {
        setTotalNumBuckets(13)
        setRedundantCopies(0)
    }

    @Bean
    internal fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>,
                                      gatewaySender: GatewaySender, customerCacheListener: CacheListener<Long, Customer>) =
            PartitionedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.PARTITION
                attributes = regionAttributes
                setGatewaySenders(arrayOf(gatewaySender))
                setCacheListeners(arrayOf(customerCacheListener))
            }

    @PeerCacheApplication
    @Profile("default", "SiteA")
    @EnableLocator(port = 10334)
    @EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[20334]")
    internal inner class SiteAWanEnabledServer {
        @Bean
        fun createGatewayReceiver(gemFireCache: GemFireCache) = GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
            setStartPort(15000)
            setEndPort(15010)
            setManualStart(false)
        }

        @Bean
        @DependsOn("DiskStore")
        fun createGatewaySender(gemFireCache: GemFireCache) = GatewaySenderFactoryBean(gemFireCache as Cache).apply {
            setBatchSize(15)
            setBatchTimeInterval(1000)
            setRemoteDistributedSystemId(2)
            isPersistent = false
            setDiskStoreRef("DiskStore")
        }
    }

    @PeerCacheApplication
    @Profile("SiteB")
    @EnableLocator(port = 20334)
    @EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]")
    internal inner class SiteBWanEnabledServer {
        @Bean
        fun createGatewayReceiver(gemFireCache: GemFireCache) = GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
            setStartPort(25000)
            setEndPort(25010)
        }

        @Bean
        @DependsOn("DiskStore")
        fun createGatewaySender(gemFireCache: GemFireCache) = GatewaySenderFactoryBean(gemFireCache as Cache).apply {
            setBatchSize(15)
            setBatchTimeInterval(1000)
            setRemoteDistributedSystemId(1)
            setDiskStoreRef("DiskStore")
        }
    }
}