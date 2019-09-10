package examples.springdata.geode.server.wan.kt.server.siteB.config

import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import org.apache.geode.cache.Cache
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean

@CacheServerApplication(port = 0, locators = "localhost[20334]",name = "SiteB_Server",enableAutoReconnect = false)
@Profile("SiteB")
@EnableLocator(port = 20334)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]",enableNetworkPartitionDetection = false)
@Import(WanEnabledServerCommonConfigKT::class)
class SiteBWanEnabledServerConfigKT {
    @Bean
    fun createGatewayReceiver(gemFireCache: GemFireCache) =
            GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
                setStartPort(25000)
                setEndPort(25010)
            }

    @Bean
    @DependsOn("DiskStore")
    fun createGatewaySender(gemFireCache: GemFireCache) =
            GatewaySenderFactoryBean(gemFireCache as Cache).apply {
                setBatchSize(15)
                setBatchTimeInterval(1000)
                setRemoteDistributedSystemId(1)
                setDiskStoreRef("DiskStore")
            }
}